package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.greet.DefaultGreetService;
import org.mca.qmass.core.event.greet.GreetEvent;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.greet.TCPGreetService;
import org.mca.qmass.core.event.leave.DefaultLeaveService;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * User: malpay
 * Date: 11.May.2011
 * Time: 11:07:29
 */
public class TCPClusterManager extends AbstractP2PClusterManager implements ClusterManager {

    private static final Log logger = LogFactory.getLog(TCPClusterManager.class);

    private SocketScannerManager scannerManager;

    private ServerSocketChannel channel;

    private InetSocketAddress listeningAt;

    private GreetService greetService;

    private LeaveService leaveService;

    private Map<InetSocketAddress, SocketChannel> acceptedChannels;

    private Map<InetSocketAddress, SocketChannel> connectedChannels;

    private QMass qmass;

    public TCPClusterManager(QMass qmass) {
        this.qmass = qmass;
        this.acceptedChannels = new HashMap<InetSocketAddress, SocketChannel>();
        this.connectedChannels = new HashMap<InetSocketAddress, SocketChannel>();
        this.scannerManager = new SocketScannerManager(qmass.getIR().getCluster());

        try {
            this.channel = ServerSocketChannel.open();
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
            } catch (Exception e) {
                socket = scanner.scan();
            }
        }

        if (listeningAt == null) {
            throw new RuntimeException("Couldnt find a free port to listen!");
        }
    }

    @Override
    public ClusterManager doSendEvent(InetSocketAddress to, Event event) {
        try {
            SocketChannel sc = connectedChannels.get(to);
            if (sc == null) {
                sc = SocketChannel.open(to);
                //sc.configureBlocking(false);
                connectedChannels.put(to, sc);
                logger.info(getId() + " connected to " + sc);
            }

            if (sc != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                new ObjectOutputStream(bos).writeObject(event);
                sc.socket().getOutputStream().write(bos.toByteArray());
                logger.debug(getId() + " send event to " + to + ", event " + event);
            } else {
                logger.info(getId() + " SocketChannel for " + to + " is null. Available connectedChannels : " + connectedChannels + " acceptedChannels : " + acceptedChannels);
            }
            return this;
        } catch (ConnectException ce) {
            logger.debug(getId() + " cant connect to " + to);
        } catch (IOException e) {
            logger.error(getId() + " had error trying to send event", e);
        }
        return this;
    }

    /**
     * Do the stuff that GreetService does
     *
     * @param closure
     * @return
     * @throws Exception
     */
    @Override
    public ClusterManager receiveEventAndDo(EventClosure closure) throws Exception {
        SocketChannel sc = channel.accept();
        if (sc != null) {
            InetSocketAddress remoteSocket = (InetSocketAddress) sc.socket().getLocalSocketAddress();
            acceptedChannels.put(remoteSocket, sc);
            logger.info(getId() + " accepted " + sc);
        }

        for (SocketChannel remoteChan : acceptedChannels.values()) {
            Event event = (Event) new ObjectInputStream(remoteChan.socket().getInputStream()).readObject();
            closure.execute(event);
        }
        return this;
    }

    @Override
    public ClusterManager end() throws IOException {
        //this.leaveService.leave();
        for (SocketChannel sc : this.acceptedChannels.values()) {
            sc.close();
        }
        for (SocketChannel sc : this.connectedChannels.values()) {
            sc.close();
        }
        this.channel.socket().close();
        return this;
    }

    @Override
    public ClusterManager start() {
        this.greetService = new DefaultGreetService(
                qmass, listeningAt, this.scannerManager.scanSocketExceptLocalPort(listeningAt.getPort()));
        this.greetService.greet();
        //this.leaveService = new DefaultLeaveService(qmass, listeningAt);
        return this;
    }

    @Override
    public Serializable getId() {
        return listeningAt;
    }

    @Override
    public InetSocketAddress getListeningAt() {
        return listeningAt;
    }

}
