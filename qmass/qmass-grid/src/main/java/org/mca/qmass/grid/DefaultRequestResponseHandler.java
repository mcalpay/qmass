package org.mca.qmass.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.grid.request.GetRequest;
import org.mca.qmass.grid.request.GetRequestResponse;
import org.mca.qmass.grid.request.PutRequest;
import org.mca.qmass.grid.request.PutRequestResponse;
import org.mca.qmass.grid.request.Request;

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
import java.util.HashMap;
import java.util.Map;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 09:55:22
 */
public class DefaultRequestResponseHandler extends Thread implements RequestResponseHandler {

    protected final Log log = LogFactory.getLog(getClass());

    private boolean runs = true;

    private DatagramChannel channel;

    private InetSocketAddress targetSocket;

    private Map<Integer, Request> responseMap = new HashMap<Integer, Request>();

    private int requestNo = 0;

    private GridMap masterGridMap;

    public DefaultRequestResponseHandler(GridMap masterGridMap, InetSocketAddress channelSocket, InetSocketAddress targetSocket) {
        this.masterGridMap = masterGridMap;
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
                        Boolean ok = this.masterGridMap.put(r.getKey(), r.getValue());
                        PutRequestResponse response = new PutRequestResponse(r.getRequestNo(), ok);
                        log.debug(this + " send : " + response);
                        send(response);
                    }

                    if (obj instanceof GetRequest) {
                        GetRequest r = (GetRequest) obj;
                        GetRequestResponse response = new GetRequestResponse(r.getRequestNo(), this.masterGridMap.get(r.getKey()));
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

    public RequestResponseHandler endWork() {
        log.debug(this + " ending");
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

    private DefaultRequestResponseHandler send(Serializable obj) {
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

    @Override
    public RequestResponseHandler startWork() {
        start();
        return this;
    }

    public Integer sendPutRequest(Serializable key, Serializable value) {
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
        return "DRRH{" + channel.socket().getLocalAddress() + ":" + channel.socket().getLocalPort() + "}";
    }
}
