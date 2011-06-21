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
package org.mca.qmass.grid.node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.grid.DefaultGrid;
import org.mca.qmass.grid.DefaultRequestResponseHandler;
import org.mca.qmass.grid.RequestResponseHandler;
import org.mca.qmass.grid.exception.TimeoutException;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.request.GetResponse;
import org.mca.qmass.grid.request.PutResponse;
import org.mca.qmass.grid.request.Response;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:33:58
 */
public class FarGridNode implements GridNode, TargetSocket {

    protected final Log log = LogFactory.getLog(getClass());

    private RequestResponseHandler defaultRequestResponseHandler;

    private InetSocketAddress targetSocket;

    private QMassGridIR ir;

    public FarGridNode(QMassGridIR ir, GridNode masterGridNode, InetSocketAddress channelSocket, InetSocketAddress targetSocket) {
        this.targetSocket = targetSocket;
        this.defaultRequestResponseHandler = new DefaultRequestResponseHandler(ir,masterGridNode,
                channelSocket,
                targetSocket);
        this.ir = ir;
        this.defaultRequestResponseHandler.startWork();
    }

    public Boolean put(Serializable key, Serializable value) {
        Serializable no = defaultRequestResponseHandler.sendPutRequest(key, value);
        if (ir.getWaitForPutResponse()) {
            PutResponse prs = (PutResponse) poll(no);
            if (prs != null) {
                return prs.isSuccessfull();
            } else {
                throw new TimeoutException("put response timed out");
            }
        }
        return Boolean.TRUE;
    }

    public Serializable get(Serializable key) {
        Serializable no = defaultRequestResponseHandler.sendGetRequest(key);
        GetResponse rh = (GetResponse) poll(no);
        if (rh != null) {
            return rh.getValue();
        } else {
            throw new TimeoutException("get response timed out");
        }
    }

    @Override
    public Serializable remove(Serializable key) {
        throw new RuntimeException("remove not supported");
    }

    public Response poll(Serializable no) {
        Response r = null;
        long start = System.currentTimeMillis();
        long timeSpent = 0L;
        do {
            r = (Response) defaultRequestResponseHandler.consumeResponse(no);
            timeSpent = System.currentTimeMillis() - start;
        } while (r == null &&
                timeSpent < ir.getResponseTimeout());
        log.debug("time spent waiting for response : " + timeSpent);
        return r;
    }

    @Override
    public InetSocketAddress getTargetSocket() {
        return targetSocket;
    }

    @Override
    public GridNode end() {
        defaultRequestResponseHandler.endWork();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        TargetSocket that = (TargetSocket) o;
        if (!targetSocket.equals(that.getTargetSocket())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return targetSocket.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        return new Integer(this.hashCode()).compareTo(new Integer(o.hashCode()));
    }
}
