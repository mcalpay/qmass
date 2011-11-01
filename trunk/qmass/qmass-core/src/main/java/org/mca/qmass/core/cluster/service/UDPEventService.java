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
import org.mca.qmass.core.ServiceIds;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.greet.DefaultGreetService;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.leave.DefaultLeaveService;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

    protected final YALog logger = YALogFactory.getLog(getClass());

    private UDPChannelService channelService;

    private DiscoveryService discoveryService;

    protected GreetService greetService;

    private LeaveService leaveService;

    public UDPEventService(QMass qmass, DiscoveryService discoveryService) {
        SocketScannerManager socketScannerManager = new SocketScannerManager(qmass.getIR().getCluster());
        this.channelService = new DefaultUDPChannelService(qmass, socketScannerManager);
        channelService.startListening();
        Scanner scanner = socketScannerManager
                .scanSocketExceptLocalPort(channelService.getListening().getPort());
        this.greetService = new DefaultGreetService(qmass, this, scanner);
        this.leaveService = new DefaultLeaveService(qmass, this);
        this.discoveryService = discoveryService;
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
        logger.trace(getListening() + " sending " + event + " to " + to);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
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
        } catch (IOException e) {
            logger.error("error sending " + event + ", to " + to);
        }
    }

    @Override
    public void start() {
        this.greetService.greet();
    }

    @Override
    public void end() throws IOException {
        leaveService.leave();
        channelService.end();
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
            logger.trace(getListening() + " received " + event);
            closure.execute(event);
            buffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
        }
    }

    @Override
    public Serializable getId() {
        return ServiceIds.DISCOVERYEVENTSERVICE;
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
    public InetSocketAddress getListening() {
        return channelService.getListening();
    }

}
