package org.mca.qmass.mongodb;

import com.mongodb.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.service.DiscoveryService;
import org.mca.qmass.core.cluster.service.EventService;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.greet.DefaultGreetService;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.leave.DefaultLeaveService;
import org.mca.qmass.core.event.leave.LeaveService;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: malpay
 * Date: 14.08.2011
 * Time: 17:39
 */
public class MongoDiscoveryEventService implements EventService {

    private static final Log logger = LogFactory.getLog(MongoDiscoveryEventService.class);

    private DiscoveryService discoveryService;

    private DBCollection collection;

    private InetSocketAddress listening;

    private GreetService greetService;

    private LeaveService leaveService;

    public MongoDiscoveryEventService(QMass qmass, DiscoveryService discoveryService,
                                      InetSocketAddress listening) {
        try {
            Mongo mongo = new Mongo();
            DB db = mongo.getDB(qmass.getId().toString());
            collection = db.getCollection("qmass_discovery");
            this.listening = listening;
            DBObject listeningObj = new BasicDBObject().append("host", listening.getHostName())
                    .append("port", listening.getPort());
            if (collection.find(listeningObj).count() == 0) {
                collection.insert(listeningObj);
            }

            this.discoveryService = discoveryService;
            this.greetService = new DefaultGreetService(qmass, this);
            this.leaveService = new DefaultLeaveService(qmass, this);
            qmass.addEventManager(this);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEvent(Event event) {
    }

    @Override
    public void sendEvent(InetSocketAddress to, Event event) {
    }

    @Override
    public InetSocketAddress getListening() {
        return listening;
    }

    @Override
    public void start() {
        DBCursor curs = collection.find();
        while (curs.hasNext()) {
            DBObject obj = curs.next();
            InetSocketAddress sock = new InetSocketAddress((String) obj.get("host"), (Integer) obj.get("port"));
            if (!listening.equals(sock)) {
                greetService.welcome(sock, new InetSocketAddress[]{});
            }
        }
    }

    @Override
    public void end() throws IOException {
        DBObject listeningObj = new BasicDBObject().append("host", listening.getHostName())
                .append("port", listening.getPort());
        collection.remove(listeningObj);
        listeningObj = null;
        collection = null;
    }

    @Override
    public void addToCluster(InetSocketAddress listeningAt) {
        discoveryService.addToCluster(listeningAt);
    }

    @Override
    public void removeFromCluster(InetSocketAddress who) {
        discoveryService.removeFromCluster(who);
    }

    @Override
    public InetSocketAddress[] getCluster() {
        return discoveryService.getCluster();
    }

    @Override
    public void receiveEventAndDo(EventClosure closure) throws Exception {
        List<InetSocketAddress> cluster
                = new ArrayList<InetSocketAddress>(Arrays.asList(discoveryService.getCluster()));
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            InetSocketAddress sock = new InetSocketAddress((String) obj.get("host"), (Integer) obj.get("port"));
            if (cluster.contains(sock)) {
                cluster.remove(sock);
            } else if (!sock.equals(listening)) {
                greetService.welcome(sock, new InetSocketAddress[]{});
            }
        }

        for (InetSocketAddress r : cluster) {
            leaveService.removeFromCluster(r);
        }

        //Thread.sleep(100);
    }

    @Override
    public Serializable getId() {
        return MongoDiscoveryEventService.class;
    }
}
