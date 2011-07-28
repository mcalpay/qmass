package org.mca.qmass.core.cluster.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * User: malpay
 * Date: 27.Tem.2011
 * Time: 22:54:09
 *
 * @TODO does not support divided packets yet
 */
public class UDPEventService implements EventService {

    protected final Log logger = LogFactory.getLog(getClass());

    private ChannelService channelService;

    private DiscoveryService discoveryService;

    public UDPEventService() {
        channelService.listenForDatagrams();
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
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(event);
        byte[] data = bos.toByteArray();
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        DatagramChannel channel = channelService.getDatagramChannel();
        int sent = channel.send(buffer, to);
        if (sent != buffer.capacity()) {
            logger.warn("sent " + sent + " bytes of " + buffer.capacity() + " to " + to);
        }
    }

    @Override
    public void receiveEventAndDo(EventClosure closure) throws Exception {
        DatagramChannel channel = channelService.getDatagramChannel();
        ByteBuffer buffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
        while (channel.receive(buffer) != null) {
            buffer.flip();
            byte[] buf = new byte[buffer.remaining()];
            buffer.get(buf);
            Event event = (Event) new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
            closure.execute(event);
            buffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
        }
    }
}
