package org.mca.qmass.core.cluster.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.id.DefaultIdGenerator;
import org.mca.qmass.core.id.IdGenerator;
import org.mca.qmass.core.serialization.JavaSerializationStrategy;
import org.mca.qmass.core.serialization.SerializationStrategy;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: malpay
 * Date: 27.Tem.2011
 * Time: 15:19:43
 */
public class TCPEventService implements EventService {

    private static final Log logger = LogFactory.getLog(TCPEventService.class);

    private TCPChannelService channelService;

    private DiscoveryService discoveryService;

    private Map<SocketChannel, Map<Integer, ByteBuffer>> objBufferHolder
            = new HashMap<SocketChannel, Map<Integer, ByteBuffer>>();

    private SerializationStrategy serializationStrategy = new JavaSerializationStrategy();

    private IdGenerator idGenerator = new DefaultIdGenerator();

    private QMass qmass;

    public TCPEventService() {
        channelService.startListening();
    }

    @Override
    public void sendEvent(Event event) throws IOException {
        InetSocketAddress[] cluster = discoveryService.getCluster();
        for (InetSocketAddress to : cluster) {
            sendEvent(to, event);
        }
    }

    @Override
    public void sendEvent(InetSocketAddress to, Event event) throws IOException {
        SocketChannel sc = channelService.getConnectedChannel(to);
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
                sc.write(buffer);
                buffer.flip();
                offset += length;
            }
        }
    }

    @Override
    public void receiveEventAndDo(EventClosure closure) throws Exception {
        List<SocketChannel> readyChannels = channelService.getReadableSocketChannels();
        for (SocketChannel sc : readyChannels) {
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
                    closure.execute(event);
                    objBufferMap.put(id, null);
                }
            }
        }
    }

    private int getTCPChunkSize() {
        return qmass.getIR().getTCPChunkSize();
    }

    @Override
    public Serializable getId() {
        return TCPEventService.class;
    }
}