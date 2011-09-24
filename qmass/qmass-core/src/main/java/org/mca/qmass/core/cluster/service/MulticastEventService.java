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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.greet.DefaultGreetService;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.leave.DefaultLeaveService;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.ir.QMassIR;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 09:45:12
 */
public class MulticastEventService implements EventService {

    private static final Log logger = LogFactory.getLog(MulticastEventService.class);

    private MulticastSocket inSocket;

    private DatagramSocket outSocket;

    private int writePort;

    private int readPort;

    private InetAddress clusterAddress;

    private InetSocketAddress listening;

    private DiscoveryService discoveryService;

    private GreetService greetService;

    private LeaveService leaveService;

    @Override
    public InetSocketAddress getListening() {
        return listening;
    }

    public MulticastEventService(QMass qmass, DiscoveryService discoveryService,
                                   InetSocketAddress listening) {
        QMassIR ir = qmass.getIR();
        try {
            clusterAddress = InetAddress.getByName(ir.getMulticastAddress());
            readPort = ir.getMulticastReadPort();
            writePort = ir.getMulticastWritePort();
            outSocket = createDatagramSocket(writePort);
            inSocket = new MulticastSocket(readPort);
            inSocket.joinGroup(clusterAddress);
            inSocket.setSoTimeout(1);

            this.listening = listening;
            this.greetService = new DefaultGreetService(qmass, this);
            this.leaveService = new DefaultLeaveService(qmass, this);

            this.discoveryService = discoveryService;
            qmass.registerService(this);
            logger.info(getListening() + " multicast " + clusterAddress + " read " + readPort + ", write " + writePort);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DatagramSocket createDatagramSocket(int writePort) {
        try {
            return new DatagramSocket(writePort);
        } catch (SocketException e) {
            return createDatagramSocket(writePort + 1);
        }
    }

    @Override
    public void sendEvent(Event event) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new ObjectOutputStream(bos).writeObject(event);
            byte[] data = bos.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, clusterAddress, readPort);
            inSocket.send(packet);
        } catch (IOException e) {
            logger.error(getId() + " had error sending event " + event, e);
        }
    }

    @Override
    public void receiveEventAndDo(EventClosure closure) throws Exception {
        try {
            while (true) {
                int size = inSocket.getReceiveBufferSize();
                byte[] buf = new byte[size];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                inSocket.receive(packet);
                Event event = (Event) new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
                closure.execute(event);
            }
        } catch (SocketTimeoutException e) {
        }
    }

    @Override
    public void end() throws IOException {
        leaveService.leave();
        outSocket.close();
        inSocket.leaveGroup(clusterAddress);
        inSocket.close();
    }

    @Override
    public void start() {
        this.greetService.greet();
    }

    @Override
    public Serializable getId() {
        return MulticastEventService.class;
    }

    @Override
    public void sendEvent(InetSocketAddress to, Event event) {
        sendEvent(event);
    }

    @Override
    public void addToCluster(InetSocketAddress who) {
        discoveryService.addToCluster(who);
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