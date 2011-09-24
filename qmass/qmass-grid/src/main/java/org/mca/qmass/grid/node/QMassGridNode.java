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
import org.mca.ir.IR;
import org.mca.ir.IRKey;
import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.DefaultGrid;
import org.mca.qmass.grid.Filter;
import org.mca.qmass.grid.QMassGrid;
import org.mca.qmass.grid.event.*;
import org.mca.qmass.grid.exception.TimeoutException;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.request.Response;
import org.mca.qmass.grid.service.DefaultGridService;
import org.mca.qmass.grid.service.GridId;
import org.mca.qmass.grid.service.GridService;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 10:55:14
 */
public class QMassGridNode implements GridNode, TargetSocket {

    protected final Log log = LogFactory.getLog(getClass());

    private GridService service;

    private InetSocketAddress targetSocket;

    private Serializable qmassId;

    public QMassGridNode(Serializable var, QMass qmass, GridNode masterGridNode, InetSocketAddress targetSocket) {
        this.service = new DefaultGridService(qmass, masterGridNode, new GridId(var, targetSocket));
        this.targetSocket = targetSocket;
        this.qmassId = qmass.getId();
    }

    @Override
    public void merge(Serializable key, Serializable value) {
        Serializable no = service.sendMerge(key, value);
        if (getIR().getWaitForPutResponse()) {
            MergeResponseEvent mrs = (MergeResponseEvent) poll(no);
            if (mrs == null) {
                throw new TimeoutException("put response timed out");
            }
        }
    }

    @Override
    public Boolean put(Serializable key, Serializable value) {
        Serializable no = service.sendPut(key, value);
        if (getIR().getWaitForPutResponse()) {
            PutResponseEvent prs = (PutResponseEvent) poll(no);
            if (prs != null) {
                return prs.isSuccessfull();
            } else {
                throw new TimeoutException("put response timed out");
            }
        }
        return Boolean.TRUE;
    }

    private QMassGridIR getIR() {
        return IR.get(new IRKey(qmassId, QMassGrid.QMASS_GRID_IR));
    }

    /**
     * @param no
     * @return
     */
    private Response poll(Serializable no) {
        Response r = null;
        long timeSpent = 0L;
        r = service.consumeResponse(no);
        return r;
    }

    @Override
    public Serializable get(Serializable key) {
        Serializable no = service.sendGet(key);
        GetResponseEvent rh = (GetResponseEvent) poll(no);
        if (rh != null) {
            return rh.getValue();
        } else {
            throw new TimeoutException("get response timed out");
        }
    }

    @Override
    public Serializable remove(Serializable key) {
        Serializable no = service.sendRemove(key);
        RemoveResponseEvent rh = (RemoveResponseEvent) poll(no);
        if (rh != null) {
            return rh.getValue();
        } else {
            throw new TimeoutException("get response timed out");
        }
    }

    @Override
    public Set<Map.Entry<Serializable, Serializable>> filter(Filter filter) {
        Serializable no = service.sendFilter(filter);
        FilterResponseEvent response = (FilterResponseEvent) poll(no);
        if (response != null) {
            return (Set<Map.Entry<Serializable, Serializable>>) response.getValue();
        } else {
            throw new TimeoutException("get response timed out");
        }
    }

    @Override
    public InetSocketAddress getTargetSocket() {
        return targetSocket;
    }

    @Override
    public GridNode end() {
        QMass.getQMass(this.qmassId).unRegisterService(service);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        InetSocketAddress otherSocket;
        if (o instanceof TargetSocket) {
            TargetSocket that = (TargetSocket) o;
            otherSocket = that.getTargetSocket();
        } else if (o instanceof InetSocketAddress) {
            otherSocket = (InetSocketAddress) o;
        } else {
            return false;
        }

        if (!targetSocket.equals(otherSocket)) return false;
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

    @Override
    public String toString() {
        return "QMassGridNode{" +
                "targetSocket=" + targetSocket +
                '}';
    }
}
