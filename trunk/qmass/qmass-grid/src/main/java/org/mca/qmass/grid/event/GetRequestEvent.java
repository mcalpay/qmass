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
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.grid.node.LocalGridNode;
import org.mca.qmass.grid.request.Request;
import org.mca.qmass.grid.service.DefaultGridService;
import org.mca.qmass.grid.service.GridId;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 09:49:28
 */
public class GetRequestEvent extends Event implements Request {

    private Serializable requestNo;

    private Serializable key;

    public GetRequestEvent(QMass qm, Serializable serviceId, Serializable requestNo, Serializable key) {
        super(qm.getId(), serviceId, GetRequestEventHandler.class.getName());
        this.requestNo = requestNo;
        this.key = key;
    }

    public Serializable getRequestNo() {
        return requestNo;
    }

    public Serializable getKey() {
        return key;
    }

    @Override
    public Service createService() {
        QMass qmass = QMass.getQMass(getId());
        LocalGridNode masterGridNode = new LocalGridNode(((DatagramClusterManager) qmass.getClusterManager()).getListeningAt());
        return new DefaultGridService(qmass, masterGridNode, (GridId) getServiceId());
    }

    @Override
    public boolean createServiceOnEvent() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetRequestEvent that = (GetRequestEvent) o;

        if (!key.equals(that.key)) return false;
        if (!requestNo.equals(that.requestNo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestNo.hashCode();
        result = 31 * result + key.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Get{" +
                "requestNo=" + requestNo +
                ", key=" + key +
                "}";
    }
}
