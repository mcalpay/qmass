package org.mca.qmass.grid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:33:58
 */
public class FarGrid implements Grid {

    private DatagramChannel channel;

    private InetSocketAddress targetSocket;

    private Worker rr;

    public FarGrid(Grid masterGrid, InetSocketAddress channelSocket, InetSocketAddress targetSocket) {
        try {
            this.channel = DatagramChannel.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.channel.socket().bind(channelSocket);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        this.targetSocket = targetSocket;
        this.rr = new Worker(masterGrid, this.channel, targetSocket);
        this.rr.start();
    }

    // send put request
    // there should be a put listener on the other hand listening to such request

    public Grid put(Serializable key, Serializable value) {
        int no = rr.sendPutRequest(key, value);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new ObjectOutputStream(bos).writeObject(new KeyValue(key, value));
            byte[] data = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            channel.send(buffer, targetSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    // send get request wait for response

    public Serializable get(Serializable key) {
        int no = rr.sendGetRequest(key);
        GetRequestResponse rh = null;
        do {
            rh = (GetRequestResponse) rr.response(no);
        } while (rh != null);
        return rh.getValue();
    }

}
