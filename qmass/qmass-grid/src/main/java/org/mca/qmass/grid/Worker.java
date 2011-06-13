package org.mca.qmass.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 09:55:22
 */
public class Worker extends Thread {

    protected final Log log = LogFactory.getLog(getClass());

    private boolean runs = true;

    private DatagramChannel channel;

    private InetSocketAddress listenSocket;

    private Map<Integer, Request> responseMap = new HashMap<Integer, Request>();

    private int requestNo = 0;

    private Grid masterGrid;

    public Worker(Grid masterGrid, DatagramChannel channel, InetSocketAddress listenSocket) {
        this.masterGrid = masterGrid;
        this.channel = channel;
        this.listenSocket = listenSocket;
    }

    @Override
    public void run() {
        while (runs) {
            try {
                int bufferSize = this.channel.socket().getReceiveBufferSize();
                ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                if (this.channel.receive(buffer) != null) {
                    log.debug("bufferSize : " + bufferSize);
                    buffer.flip();
                    byte[] buf = new byte[buffer.remaining()];
                    buffer.get(buf);
                    Object obj = (Object) new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
                    if (obj instanceof Request) {
                        Request r = (Request) obj;
                        responseMap.put(r.getRequestNo(), r);
                    }

                    if (obj instanceof PutRequest) {
                        PutRequest r = (PutRequest) obj;
                        this.masterGrid.put(r.getKey(), r.getValue());
                    }

                    log.debug("object recieved : " + obj);
                }
            } catch (Exception e) {
                // Log the error appopriately
                e.printStackTrace();
            }
        }
    }

    public Worker end() {
        runs = false;
        return this;
    }

    public Integer sendGetRequest(Serializable key) {
        log.debug("send get for : " + key);
        int no = requestNo++;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new ObjectOutputStream(bos).writeObject(new GetRequest(no, key));
            byte[] data = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            buffer.flip();
            channel.send(buffer, listenSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return no;
    }

    public int sendPutRequest(Serializable key, Serializable value) {
        log.debug("send put for : " + key + ", " + value);
        int no = requestNo++;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new ObjectOutputStream(bos).writeObject(new PutRequest(no, key, value));
            byte[] data = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            buffer.flip();
            channel.send(buffer, listenSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return no;
    }

    public Request response(int no) {
        return responseMap.get(no);
    }
}
