package org.mca.qmass.mongodb;

import com.mongodb.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.env.MongoServiceInfo;
import org.cloudfoundry.runtime.service.AbstractServiceCreator;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.service.DiscoveryService;
import org.mca.qmass.core.cluster.service.EventService;
import org.mca.qmass.core.cluster.service.UDPEventService;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.greet.DefaultGreetService;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.leave.DefaultLeaveService;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.scanner.Scanner;
import org.springframework.data.document.mongodb.MongoDbFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * User: malpay
 * Date: 14.08.2011
 * Time: 17:39
 */
public class MongoDiscoveryEventService extends UDPEventService {

    private DBCollection collection;

    public MongoDiscoveryEventService(QMass qmass, DiscoveryService discoveryService) {
        super(qmass, discoveryService);
        logger.debug("cloud env\n " + System.getenv());
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        MongoDbFactory f = new MongoServiceCreator(cloudEnvironment).createSingletonService().service;
        DB db = f.getDb();
        collection = db.getCollection("qmass_discovery");
        DBObject listeningObj = new BasicDBObject().append("host", getListening().getHostName())
                .append("port", getListening().getPort());
        if (collection.find(listeningObj).count() == 0) {
            collection.insert(listeningObj);
        }
    }

    @Override
    public void start() {
        final List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
        DBCursor curs = collection.find();
        while (curs.hasNext()) {
            DBObject obj = curs.next();
            InetSocketAddress sock = new InetSocketAddress((String) obj.get("host"), (Integer) obj.get("port"));
            if (!getListening().equals(sock)) {
                list.add(sock);
            }
        }
        this.greetService.setScanner(new Scanner() {
            Iterator<InetSocketAddress> iter = list.iterator();

            @Override
            public InetSocketAddress scan() {
                while (iter.hasNext()) {
                    return iter.next();
                }
                return null;
            }
        });
        super.start();
    }

    @Override
    public void end() throws IOException {
        DBObject listeningObj = new BasicDBObject().append("host", getListening().getHostName())
                .append("port", getListening().getPort());
        collection.remove(listeningObj);
        super.end();
    }

    @Override
    public Serializable getId() {
        return MongoDiscoveryEventService.class;
    }
}