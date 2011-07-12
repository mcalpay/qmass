package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.greet.DefaultGreetService;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.leave.DefaultLeaveService;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: malpay
 * Date: 11.May.2011
 * Time: 11:07:29
 */
public class SocketClusterManager extends AbstractP2PClusterManager implements ClusterManager {

    private static final Log logger = LogFactory.getLog(SocketClusterManager.class);

    private SocketScannerManager scannerManager;

    private ServerSocketChannel serverChannel;

    private InetSocketAddress serverListening;

    private DatagramChannel clusterChannel;

    private InetSocketAddress clusterListening;

    private GreetService greetService;

    private LeaveService leaveService;

    private Map<InetSocketAddress,SocketChannel> clusterChannels;

    private QMass qmass;

    public SocketClusterManager() {
        qmass = QMass.getQMass();
        clusterChannels = new HashMap<InetSocketAddress,SocketChannel>();
        SocketScannerManager ssm = new SocketScannerManager(QMass.getQMass().getIR().getCluster());
        configureServerChannel(ssm);
        configureClusterChannel(ssm);

        if (serverListening == null || clusterListening == null) {
            throw new RuntimeException("Couldnt find a free port to listen!");
        }

    }

    private void configureClusterChannel(SocketScannerManager scannerManager) {
        try {
            clusterChannel = DatagramChannel.open();
            clusterChannel.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = scannerManager.scanLocalSocket();
        InetSocketAddress socket = scanner.scan();
        while (socket != null) {
            try {
                this.clusterChannel.socket().bind(socket);
                clusterListening = socket;
                break;
            } catch (IOException e) {
                socket = scanner.scan();
            }
        }
    }

    private void configureServerChannel(SocketScannerManager scannerManager) {
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = scannerManager.scanLocalSocket();
        InetSocketAddress socket = scanner.scan();
        while (socket != null) {
            try {
                this.serverChannel.socket().bind(socket);
                serverListening = socket;
                break;
            } catch (IOException e) {
                socket = scanner.scan();
            }
        }
    }

    @Override
    public P2PClusterManager doAddToCluster(InetSocketAddress who) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(who);
        cluster.add(who);
        clusterChannels.put(who,sc);
        return this;
    }

    @Override
    protected P2PClusterManager doRemoveFromCluster(InetSocketAddress who) {
        SocketChannel sc = clusterChannels.get(who);
        clusterChannels.remove(who);
        try {
            sc.close();
        } catch (IOException e) {
            logger.error(getId() + ", error closing channel : " + sc, e);
        }
        return this;
    }

    @Override
    public P2PClusterManager doSendEvent(InetSocketAddress to, Event event) {
        try {
            logger.debug(serverListening + ", " + qmass.getId() + " sending " + event + " to " + to);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new ObjectOutputStream(bos).writeObject(event);
            byte[] data = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            buffer.flip();
            int sent = clusterChannel.send(buffer, to);
            if (sent != buffer.capacity()) {
                logger.warn(serverListening + ", " + qmass.getId() + "sent " + sent + " bytes of " + buffer.capacity() + " to " + to);
            }
            return this;
        } catch (IOException e) {
            logger.error(getId() + " had error trying to send event", e);
        }
        return this;
    }

    @Override
    public ClusterManager sendEvent(Event event) throws IOException {
        return null;
    }

    @Override
    public ClusterManager receiveEventAndDo(EventClosure closure) throws Exception {
        return null;
    }

    @Override
    public ClusterManager end() throws IOException {
        return null;
    }

    @Override
    public ClusterManager start() {
        this.greetService = new DefaultGreetService(
                qmass, serverListening, clusterListening, this.scannerManager.scanSocketExceptLocalPort(serverListening.getPort()));
        this.greetService.greet();
        this.leaveService = new DefaultLeaveService(qmass, serverListening);
        return this;
    }

    @Override
    public Serializable getId() {
        return serverListening;
    }

}
