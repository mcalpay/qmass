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
package org.mca.qmass.core.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 17:37:40
 */
public class Event implements Serializable {

    private Serializable id;

    private Serializable serviceId;

    private String handlerName;

    public Event(QMass qm, Service service, Class handler) {
        this(qm.getId(), service.getId(), handler.getName());
    }

    public Event(Serializable id, Serializable serviceId, String handlerName) {
        this.id = id;
        this.serviceId = serviceId;
        this.handlerName = handlerName;
    }

    public Event() {
    }

    public Serializable getId() {
        return id;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public Serializable getServiceId() {
        return serviceId;
    }

    public boolean createServiceOnEvent() {
        return false;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", handler=" + ((handlerName == null) ? null :
                handlerName.substring(handlerName.lastIndexOf('.') + 1)) +
                ", service=" + serviceId +
                '}';
    }

    public Service createService(QMass q) {
        return NOOPService.getInstance();
    }
}
