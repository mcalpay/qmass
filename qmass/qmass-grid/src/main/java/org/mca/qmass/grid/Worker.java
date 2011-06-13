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

    private InetSocketAddress targetSocket;

    private Map<Integer, Request> responseMap = new HashMap<Integer, Request>();

    private int requestNo = 0;

    private Grid masterGrid;

    public Worker(Grid masterGrid, DatagramChannel channel, InetSocketAddress targetSocket) {
        this.masterGrid = masterGrid;
        this.channel = channel;
        this.targetSocket = targetSocket;
    }

    @Override
    public void run() {
        while (runs) {
            try {
                int bufferSize = this.channel.socket().getReceiveBufferSize();
                ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                if (this.channel.receive(buffer) != null) {
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

                    if (obj instanceof GetRequest) {
                        GetRequest r = (GetRequest) obj;
                        GetRequestResponse response = new GetRequestResponse(r.getRequestNo(), this.masterGrid.get(r.getKey()));
                        log.debug(this + " send : " + response);
                        send(response);
                    }


                    log.debug(this + " object recieved : " + obj);
                }
            } catch (Exception e) {
                log.error(this, e);
            }
        }
    }

    public Worker end() {
        runs = false;
        try {
            this.channel.close();
        } catch (IOException e) {
            log.error(this, e);
        }
        return this;
    }

    public Integer sendGetRequest(Serializable key) {
        log.debug(this + " send get for : " + key);
        int no = getRequestNo();
        GetRequest getRequest = new GetRequest(no, key);
        send(getRequest);
        return no;
    }

    private Worker send(Serializable obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new ObjectOutputStream(bos).writeObject(obj);
            byte[] data = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            buffer.flip();
            channel.send(buffer, targetSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public int sendPutRequest(Serializable key, Serializable value) {
        log.debug(this + " send put for : " + key + ", " + value);
        int no = getRequestNo();
        PutRequest putRequest = new PutRequest(no, key, value);
        send(putRequest);
        return no;
    }

    private int getRequestNo() {
        return ++requestNo;
    }

    public Request response(int no) {
        return responseMap.get(no);
    }

    @Override
    public String toString() {
        return "Worker{" + channel.socket().getLocalAddress() + ":" + channel.socket().getLocalPort() + "}";
    }
}
