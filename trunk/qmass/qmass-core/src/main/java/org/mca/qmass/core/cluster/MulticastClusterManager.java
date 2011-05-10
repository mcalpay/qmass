package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.AbstractEvent;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;

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
import java.nio.ByteBuffer;

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
            outSocket = new DatagramSocket(writePort);
            inSocket = new MulticastSocket(readPort);
            inSocket.joinGroup(clusterAddress);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    public ClusterManager receiveEvent(EventClosure closure) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(this.inSocket.getReceiveBufferSize());
        while (this.inSocket.getChannel().receive(buffer) != null) {
            buffer.flip();     
            byte[] buf = new byte[buffer.remaining()];
            buffer.get(buf);
            AbstractEvent event = (AbstractEvent) new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
            closure.execute(event);
            buffer = ByteBuffer.allocate(this.inSocket.getReceiveBufferSize());
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
