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
import org.mca.qmass.core.id.DefaultIdGenerator;
import org.mca.qmass.core.id.IdGenerator;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;
import org.mca.qmass.core.serialization.JavaSerializationStrategy;
import org.mca.qmass.core.serialization.SerializationStrategy;

import java.io.IOException;
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
 * @TODO handle channel close
 * @TODO EventService, DiscoveryService, ChannelService
 * @TODO initial read write can took too much time, tested with 3 grids
 * @TODO Reuse the ByteBuffer's
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

    private Map<SocketChannel, Map<Integer, ByteBuffer>> objBufferHolder
            = new HashMap<SocketChannel, Map<Integer, ByteBuffer>>();

    private QMass qmass;

    private Selector selector;

    private IdGenerator idGenerator;

    private SerializationStrategy serializationStrategy = new JavaSerializationStrategy();

    public TCPClusterManager(QMass qmass) {
        this.qmass = qmass;
        this.acceptedChannels = new HashMap<InetSocketAddress, SocketChannel>();
        this.connectedChannels = new HashMap<InetSocketAddress, SocketChannel>();
        this.scannerManager = new SocketScannerManager(qmass.getIR().getCluster());
        this.idGenerator = new DefaultIdGenerator();

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
    public void doSendEvent(InetSocketAddress to, Event event) throws IOException {
        logger.debug(getId() + " sending event " + event + " to " + to);
        SocketChannel sc = connectedChannels.get(to);
        if (sc == null) {
            sc = SocketChannel.open(to);
            sc.configureBlocking(false);
            sc.finishConnect();
            connectedChannels.put(to, sc);
        }

        if (sc != null) {
            int chunkSize = getTCPChunkSize();
            byte[] data = serializationStrategy.serialize(event);

            int offset = 0;
            int id = idGenerator.nextId();
            ByteBuffer buffer = ByteBuffer.allocate(chunkSize);
            while (offset < data.length) {

                buffer.putInt(id);
                buffer.putInt(data.length);

                int remainingSize = chunkSize - buffer.position();
                int length = (remainingSize + offset < data.length) ? remainingSize : data.length - offset;
                buffer.put(data, offset, length);

                buffer.position(chunkSize);

                buffer.flip();
                int wrote = sc.write(buffer);
                buffer.flip();
                offset += length;
            }

        } else {
            logger.info(getId() + " SocketChannel for " + to + " is null. Available connectedChannels : " + connectedChannels + " acceptedChannels : " + acceptedChannels);
        }
    }

    /**
     * @param closure
     * @return
     * @throws Exception
     */
    @Override
    public void receiveEventAndDo(EventClosure closure) throws Exception {
        selector.select(1);
        for (SelectionKey sk : selector.selectedKeys()) {
            if (sk.isAcceptable()) {
                SocketChannel sc = ((ServerSocketChannel) sk.channel()).accept();
                if (sc != null) {
                    sc.configureBlocking(false);
                    sc.finishConnect();
                    sc.register(selector, SelectionKey.OP_READ);
                    InetSocketAddress remoteSocket = (InetSocketAddress) sc.socket().getRemoteSocketAddress();
                    acceptedChannels.put(remoteSocket, sc);
                }
            } else if (sk.isReadable()) {
                SocketChannel sc = (SocketChannel) sk.channel();
                Map<Integer, ByteBuffer> objBufferMap = objBufferHolder.get(sc);
                if (objBufferMap == null) {
                    objBufferMap = new HashMap();
                    objBufferHolder.put(sc, objBufferMap);
                }

                ByteBuffer buffer = ByteBuffer.allocate(getTCPChunkSize());
                int red = sc.read(buffer);

                buffer.flip();
                if (red > 0) {
                    int id = buffer.getInt();
                    int length = buffer.getInt();
                    int remainingSize = getTCPChunkSize() - buffer.position();

                    ByteBuffer objBuffer = objBufferMap.get(id);
                    if (objBuffer == null) {
                        objBuffer = ByteBuffer.allocate(length);
                        objBufferMap.put(id, objBuffer);
                    }

                    byte[] buf = new byte[buffer.remaining()];
                    buffer.get(buf);
                    int remaining = (objBuffer.remaining() > remainingSize)
                            ? remainingSize : objBuffer.remaining();
                    objBuffer.put(buf, 0, remaining);

                    if (objBuffer.position() == length) {
                        objBuffer.flip();
                        buf = new byte[objBuffer.remaining()];
                        objBuffer.get(buf);

                        Event event = (Event) serializationStrategy.deSerialize(buf);
                        logger.debug(getId() + " received " + event);
                        closure.execute(event);

                        objBufferMap.put(id, null);
                    }
                }
            }
        }
    }

    @Override
    protected ClusterManager doRemoveFromCluster(InetSocketAddress who) throws IOException {
        Channel ch = this.connectedChannels.remove(who);
        ch.close();
        return this;
    }

    @Override
    public void end() throws IOException {
        this.leaveService.leave();
        for (SocketChannel sc : this.acceptedChannels.values()) {
            sc.close();
        }

        for (SocketChannel sc : this.connectedChannels.values()) {
            sc.close();
        }

        this.channel.socket().close();
    }

    @Override
    public void start() {
        this.leaveService = new DefaultLeaveService(qmass, this);
        this.greetService = new DefaultGreetService(
                qmass, this, this.scannerManager.scanSocketExceptLocalPort(listeningAt.getPort()));
        this.greetService.greet();
    }

    @Override
    public Serializable getId() {
        return listeningAt;
    }

    @Override
    public InetSocketAddress getListening() {
        return listeningAt;
    }

    private int getTCPChunkSize() {
        return qmass.getIR().getTCPChunkSize();
    }

}
