package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.AbstractEvent;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.EventHandler;
import org.mca.qmass.core.event.greet.DefaultGreetService;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.leave.DefaultLeaveService;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 15:57:38
 */
public class DatagramClusterManager implements ClusterManager {

    private static final Log logger = LogFactory.getLog(DatagramClusterManager.class);

    private QMass qmass;

    private InetSocketAddress listeningAt;

    private Set<InetSocketAddress> cluster;

    private DatagramChannel channel;

    private SocketScannerManager scannerManager;

    private GreetService greetService;

    private LeaveService leaveService;

    public DatagramClusterManager(QMass qmass) {
        this.qmass = qmass;
        this.scannerManager = new SocketScannerManager(qmass.getIR().getCluster());
        this.cluster = new HashSet<InetSocketAddress>();

        try {
            this.channel = DatagramChannel.open(); 
            this.channel.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = scannerManager.scanLocalSocket();
        InetSocketAddress socket = scanner.scan();
        while (socket != null) {
            try {
                this.channel.socket().bind(socket);
                listeningAt = socket;
                break;
            } catch (SocketException e) {
                socket = scanner.scan();
            }
        }

        if (listeningAt == null) {
            throw new RuntimeException("Couldnt find a free port to listen!");
        }
    }

    @Override
    public DatagramClusterManager sendEvent(Event event) throws IOException {
        for (InetSocketAddress to : cluster) {
            sendEvent(to, event);
        }
        return this;

    }

    @Override
    public ClusterManager receiveEvent(EventClosure closure) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(this.channel.socket().getReceiveBufferSize());
        while (this.channel.receive(buffer) != null) {
            buffer.flip();
            byte[] buf = new byte[buffer.remaining()];
            buffer.get(buf);
            AbstractEvent event = (AbstractEvent) new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
            closure.execute(event);
            buffer = ByteBuffer.allocate(this.channel.socket().getReceiveBufferSize());
        }
        return this;
    }

    @Override
    public ClusterManager end() {
        this.leaveService.leave();
        this.channel.socket().close();
        return this;
    }

    @Override
    public ClusterManager start() {
        this.greetService = new DefaultGreetService(
                qmass, listeningAt, this.scannerManager.scanSocketExceptLocalPort(listeningAt.getPort()));
        this.greetService.greet();
        this.leaveService = new DefaultLeaveService(qmass, listeningAt);
        return this;
    }

    @Override
    public Serializable getId() {
        return listeningAt;
    }

    public ClusterManager addToCluster(InetSocketAddress who) {
        cluster.add(who);
        logger.info("Cluster;\n\t" + listeningAt + "\n\t" + cluster);
        return this;
    }

    public ClusterManager removeFromCluster(InetSocketAddress who) {
        cluster.remove(who);
        logger.info("Cluster;\n\t" + listeningAt + "\n\t" + cluster);
        return this;
    }

    public InetSocketAddress[] getCluster() {
        return cluster.toArray(new InetSocketAddress[cluster.size()]);
    }

    public DatagramClusterManager sendEvent(InetSocketAddress to, Event event) throws IOException {
        logger.debug(listeningAt + ", " + qmass.getId() + " sending " + event + " to " + to);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(event);
        byte[] data = bos.toByteArray();
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        int sent = channel.send(buffer, to);
        if (sent != buffer.capacity()) {
            logger.warn(listeningAt + ", " + qmass.getId() + "sent " + sent + " bytes of " + buffer.capacity() + " to " + to);
        }
        return this;
    }

    public InetSocketAddress getListeningAt() {
        return listeningAt;
    }

    public DatagramClusterManager safeSendEvent(InetSocketAddress who, Event event) {
        try {
            return sendEvent(who, event);
        } catch (IOException e) {
            logger.error(getId() + " had error trying to send event", e);
        }
        return this;
    }
}
