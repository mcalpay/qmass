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
package org.mca.qmass.grid.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.grid.QMassGrid;
import org.mca.qmass.grid.request.Request;
import org.mca.qmass.grid.service.GridId;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 09:56:42
 */
public class PutRequestEvent extends Event implements Request {

    private Serializable requestNo;

    private Serializable key;

    private Serializable value;

    private boolean waitingForResponse = false;

    public PutRequestEvent(QMass qm, Serializable serviceId, Serializable requestNo,
                           Serializable key, Serializable value, boolean waitingForResponse) {
        super(qm.getId(), serviceId, PutRequestEventHandler.class.getName());
        this.requestNo = requestNo;
        this.key = key;
        this.value = value;
        this.waitingForResponse = waitingForResponse;
    }

    public Serializable getRequestNo() {
        return requestNo;
    }

    public Serializable getKey() {
        return key;
    }

    public Serializable getValue() {
        return value;
    }

    public boolean isWaitingForResponse() {
        return waitingForResponse;
    }

    @Override
    public Service createService() {
        QMass qmass = QMass.getQMass(getId());
        GridId var = (GridId) getServiceId();
        new QMassGrid(var.getVar(), qmass);
        return qmass.getService(var);    
    }

    @Override
    public boolean createServiceOnEvent() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PutRequestEvent that = (PutRequestEvent) o;

        if (!key.equals(that.key)) return false;
        if (!requestNo.equals(that.requestNo)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestNo.hashCode();
        result = 31 * result + key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PutRequest{" +
                "requestNo=" + requestNo +
                ", key=" + key +
                ", value=" + value +
                "} " + super.toString();
    }
}
