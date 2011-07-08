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
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 15:57:38
 */
public class DatagramClusterManager extends AbstractP2PClusterManager implements ClusterManager {

    private QMass qmass;

    private InetSocketAddress listeningAt;

    private DatagramChannel channel;

    private SocketScannerManager scannerManager;

    private GreetService greetService;

    private LeaveService leaveService;

    public DatagramClusterManager(QMass qmass) {
        this.qmass = qmass;
        this.scannerManager = new SocketScannerManager(qmass.getIR().getCluster());

        try {
            this.channel = DatagramChannel.open(); 
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
            } catch (SocketException e) {
                socket = scanner.scan();
            }
        }

        if (listeningAt == null) {
            throw new RuntimeException("Couldnt find a free port to listen!");
        }
    }

    @Override
    public DatagramClusterManager sendEvent(Event event) throws IOException {
        for (InetSocketAddress to : cluster) {
            doSendEvent(to, event);
        }
        return this;

    }

    @Override
    public ClusterManager receiveEventAndDo(EventClosure closure) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(this.channel.socket().getReceiveBufferSize());
        while (this.channel.receive(buffer) != null) {
            buffer.flip();
            byte[] buf = new byte[buffer.remaining()];
            buffer.get(buf);
            Event event = (Event) new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
            closure.execute(event);
            buffer = ByteBuffer.allocate(this.channel.socket().getReceiveBufferSize());
        }
        return this;
    }

    @Override
    public ClusterManager end() {
        this.leaveService.leave();
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

    public P2PClusterManager doSendEvent(InetSocketAddress to, Event event) throws IOException {
        logger.debug(listeningAt + ", " + qmass.getId() + " sending " + event + " to " + to);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(event);
        byte[] data = bos.toByteArray();
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        int sent = channel.send(buffer, to);
        if (sent != buffer.capacity()) {
            logger.warn(listeningAt + ", " + qmass.getId() + "sent " + sent + " bytes of " + buffer.capacity() + " to " + to);
        }
        return this;
    }

    public InetSocketAddress getListeningAt() {
        return listeningAt;
    }

}
