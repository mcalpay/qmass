/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mca.qmass.core.cluster.service;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.id.DefaultIdGenerator;
import org.mca.qmass.core.id.IdGenerator;
import org.mca.qmass.core.scanner.SocketScannerManager;
import org.mca.qmass.core.serialization.JavaSerializationStrategy;
import org.mca.qmass.core.serialization.SerializationStrategy;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * User: malpay
 * Date: 27.Tem.2011
 * Time: 15:19:43
 */
public class TCPEventService implements EventService {

    private static final YALog logger = YALogFactory.getLog(TCPEventService.class);

    private TCPChannelService channelService;

    private DiscoveryService discoveryService;

    private Map<SocketChannel, Map<Integer, ByteBuffer>> objBufferHolder
            = new HashMap<SocketChannel, Map<Integer, ByteBuffer>>();

    private SerializationStrategy serializationStrategy = new JavaSerializationStrategy();

    private IdGenerator idGenerator = new DefaultIdGenerator();

    private QMass qmass;

    private EventService discoveryEventService;

    public TCPEventService(QMass qmass) {
        this.qmass = qmass;
        SocketScannerManager socketScannerManager = new SocketScannerManager(qmass.getIR().getCluster());
        this.channelService = new DefaultTCPChannelService(qmass, socketScannerManager);
        channelService.startListening();
        this.discoveryService = new DefaultDiscoveryService(this.channelService);
        discoveryEventService =
                qmass.getIR().getDiscoveryEventService(qmass, discoveryService,
                        channelService.getListening());
        qmass.registerService(this);
    }

    @Override
    public void sendEvent(Event event) {
        InetSocketAddress[] cluster = discoveryService.getCluster();
        for (InetSocketAddress to : cluster) {
            sendEvent(to, event);
        }
    }

    @Override
    public void sendEvent(InetSocketAddress to, Event event) {
        SocketChannel sc = channelService.getConnectedChannel(to);
        if (sc != null) {
            logger.debug(getListening() + " sending " + event + " to " + to);
            try {
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
            } catch (IOException e) {
                channelService.removeConnectedChannel(to);
                LeaveService leaveService = (LeaveService) qmass.getService(LeaveService.class);
                leaveService.removeFromCluster(to);
                logger.error("error sending event" + event + ", to " + to);
                logger.trace("error sending event" + event + ", to " + to, e);
            }
        }

        logger.debug("sent " + event + ", to " + to);
    }

    @Override
    public void start() {
        //this.greetService.greet();
        discoveryEventService.start();
    }

    @Override
    public void end() throws IOException {
        //leaveService.leave();
        discoveryEventService.end();
        channelService.end();
    }

    @Override
    public void receiveEventAndDo(EventClosure closure) throws Exception {
        List<SocketChannel> readyChannels = channelService.getReadableSocketChannels();
        List<SocketChannel> channelsToRemove = new ArrayList<SocketChannel>();
        for (SocketChannel sc : readyChannels) {
            try {
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
                        logger.debug(getListening() + " received " + event);
                        closure.execute(event);
                        objBufferMap.put(id, null);
                    }
                }
            } catch (IOException e) {
                channelsToRemove.add(sc);
            }
        }

        channelService.removeFromReadableChannels(channelsToRemove);
    }

    private int getTCPChunkSize() {
        return qmass.getIR().getTCPChunkSize();
    }

    @Override
    public Serializable getId() {
        return TCPEventService.class;
    }

    @Override
    public InetSocketAddress getListening() {
        return channelService.getListening();
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
}