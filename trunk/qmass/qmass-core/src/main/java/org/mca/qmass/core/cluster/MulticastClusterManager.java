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
package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
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
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 09:45:12
 */
public class MulticastClusterManager implements ClusterManager {

    private static final Log logger = LogFactory.getLog(DatagramClusterManager.class);

    private MulticastSocket inSocket;

    private DatagramSocket outSocket;

    private int writePort;

    private int readPort;

    private InetAddress clusterAddress;

    public MulticastClusterManager() {
        try {
            clusterAddress = InetAddress.getByName("230.0.0.1");
            readPort = 4444;
            writePort = 4445;
            outSocket = createDatagramSocket(writePort);
            inSocket = new MulticastSocket(readPort);
            inSocket.joinGroup(clusterAddress);
            inSocket.setSoTimeout(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MulticastClusterManager(QMassIR ir) {
        try {
            clusterAddress = InetAddress.getByName(ir.getMulticastAddress());
            readPort = ir.getMulticastReadPort();
            writePort = ir.getMulticastWritePort();
            outSocket = createDatagramSocket(writePort);
            inSocket = new MulticastSocket(readPort);
            inSocket.joinGroup(clusterAddress);
            inSocket.setSoTimeout(1);
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
    public ClusterManager sendEvent(Event event) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(event);
        byte[] data = bos.toByteArray();
        DatagramPacket packet = new DatagramPacket(data, data.length, clusterAddress, readPort);
        inSocket.send(packet);
        return this;
    }

    @Override
    public ClusterManager receiveEventAndDo(EventClosure closure) throws Exception {
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
        return this;
    }

    @Override
    public ClusterManager end() throws IOException {
        outSocket.close();
        inSocket.leaveGroup(clusterAddress);
        inSocket.close();
        return this;
    }

    @Override
    public ClusterManager start() {
        return this;
    }

    @Override
    public Serializable getId() {
        return "multicast";
    }
}