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
package org.mca.qmass.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.grid.id.DefaultIdGenerator;
import org.mca.qmass.grid.id.IdGenerator;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.request.GetRequest;
import org.mca.qmass.grid.request.GetResponse;
import org.mca.qmass.grid.request.PutRequest;
import org.mca.qmass.grid.request.PutResponse;
import org.mca.qmass.grid.request.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<Serializable, Response> responseMap = new ConcurrentHashMap<Serializable, Response>();

    private GridNode masterGridNode;

    private IdGenerator idGenerator;

    public DefaultRequestResponseHandler(GridNode masterGridNode, InetSocketAddress channelSocket,
                                         InetSocketAddress targetSocket) {
        this.masterGridNode = masterGridNode;
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
        this.idGenerator = new DefaultIdGenerator(channelSocket);
    }

    public DefaultRequestResponseHandler(Serializable id, GridNode masterGridNode, DatagramChannel channel,
                                         InetSocketAddress targetSocket) {
        this.masterGridNode = masterGridNode;
        this.channel = channel;
        this.targetSocket = targetSocket;
        this.idGenerator = new DefaultIdGenerator(id);
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
                    if (obj instanceof Response) {
                        Response r = (Response) obj;
                        log.debug(this + " response : " + r);
                        responseMap.put(r.getRequestNo(), r);
                    }

                    if (obj instanceof PutRequest) {
                        PutRequest r = (PutRequest) obj;
                        Boolean ok = this.masterGridNode.put(r.getKey(), r.getValue());
                        if (r.isWaitingForResponse()) {
                            PutResponse response = new PutResponse(r.getRequestNo(), ok);
                            log.debug(this + " send : " + response);
                            send(response);
                        }
                    }

                    if (obj instanceof GetRequest) {
                        GetRequest r = (GetRequest) obj;
                        GetResponse response = new GetResponse(r.getRequestNo(), this.masterGridNode.get(r.getKey()));
                        log.debug(this + " send : " + response);
                        send(response);
                    }

                    log.debug(this + " object recieved : " + obj);
                }
            } catch (Exception e) {
                if (!(e instanceof AsynchronousCloseException)) {
                    log.error(this, e);
                }
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

    public Serializable sendGetRequest(Serializable key) {
        log.debug(this + " send get for : " + key);
        Serializable no = getRequestNo();
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

    public Serializable sendPutRequest(Serializable key, Serializable value) {
        log.debug(this + " send put for : " + key + ", " + value);
        Serializable no = getRequestNo();
        PutRequest putRequest = new PutRequest(no, key, value);
        send(putRequest);
        return no;
    }

    // probably should be synchronised ...

    private Serializable getRequestNo() {
        return this.idGenerator.nextId();
    }

    @Override
    public Response consumeResponse(Serializable no) {
        log.trace(this + " consuming response " + no);
        return responseMap.remove(no);
    }

    @Override
    public String toString() {
        return "{" + channel.socket().getLocalAddress() + ":" + channel.socket().getLocalPort() + "}";
    }
}
