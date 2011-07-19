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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @TODO an event divided into multiple chunks, channel close, LeaveService
 * initial read write can took too much time, tested with 3 grids
 * <p/>
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

    private Selector selector;

    public TCPClusterManager(QMass qmass) {

        this.qmass = qmass;
        this.acceptedChannels = new HashMap<InetSocketAddress, SocketChannel>();
        this.connectedChannels = new HashMap<InetSocketAddress, SocketChannel>();
        this.scannerManager = new SocketScannerManager(qmass.getIR().getCluster());

        try {
            this.selector = Selector.open();
            this.channel = ServerSocketChannel.open();
            this.channel.configureBlocking(false);
            this.channel.register(this.selector, SelectionKey.OP_ACCEPT);
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
                sc.configureBlocking(false);
                connectedChannels.put(to, sc);
                logger.info(getId() + " connected to " + sc + ", " + sc.isConnectionPending() + ", " + sc.isConnected());
            }

            if (sc != null) {
                int chunkSize = getTCPChunkSize();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                new ObjectOutputStream(bos).writeObject(event);

                ByteBuffer buffer = ByteBuffer.allocate(chunkSize);

                byte[] data = bos.toByteArray();

                logger.debug(getId() + ", length : " + data.length);

                buffer.putInt(data.length);
                buffer.put(data);
                buffer.position(chunkSize);
                buffer.flip();

                int wrote = sc.write(buffer);

                logger.debug(getId() + " wrote : " + wrote);
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
     * TODO move to IR
     *
     * @return
     */
    private int getTCPChunkSize() {
        return 2048;
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
        selector.select();
        for (SelectionKey sk : selector.selectedKeys()) {
            if (sk.isAcceptable()) {
                SocketChannel sc = ((ServerSocketChannel) sk.channel()).accept();
                if (sc != null) {
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                    InetSocketAddress remoteSocket = (InetSocketAddress) sc.socket().getRemoteSocketAddress();
                    acceptedChannels.put(remoteSocket, sc);
                    logger.info(getId() + " accepted "  + sc + ", " + sc.isConnectionPending() + ", " + sc.isConnected());
                }
            } else if (sk.isReadable()) {
                SocketChannel sc = (SocketChannel) sk.channel();
                ByteBuffer buffer = ByteBuffer.allocate(getTCPChunkSize());
                int red = sc.read(buffer);
                if (red > 0) {
                    logger.debug(getId() + " red : " + red);
                    buffer.flip();
                    int length = buffer.getInt();
                    logger.debug(getId() + " length : " + length);
                    byte[] buf = new byte[buffer.remaining()];
                    buffer.get(buf);
                    Event event = (Event) new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
                    closure.execute(event);
                }
            }
        }
        return this;
    }

    @Override
    protected ClusterManager doRemoveFromCluster(InetSocketAddress who) throws IOException {
        Channel ch = this.connectedChannels.remove(who);
        ch.close();
        /*ch = this.acceptedChannels.remove(who);
        ch.close();*/
        return this;
    }

    @Override
    public ClusterManager end() throws IOException {
        this.leaveService.leave();
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
        this.leaveService = new DefaultLeaveService(qmass, listeningAt);
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
