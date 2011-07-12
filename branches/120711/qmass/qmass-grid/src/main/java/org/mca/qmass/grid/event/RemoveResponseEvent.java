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
import org.mca.qmass.core.event.Event;
import org.mca.qmass.grid.request.Response;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 20.Haz.2011
 * Time: 15:19:41
 */
public class RemoveResponseEvent extends Event implements Response {

    private Serializable requestNo;

    private Serializable value;

    public RemoveResponseEvent(QMass qm, Serializable serviceId, Serializable requestNo,
                               Serializable value) {
        super(qm.getId(), serviceId, RemoveResponseEventHandler.class.getName());
        this.requestNo = requestNo;
        this.value = value;
    }

    public Serializable getRequestNo() {
        return requestNo;
    }

    public Serializable getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemoveResponseEvent that = (RemoveResponseEvent) o;

        if (!requestNo.equals(that.requestNo)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestNo.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RemoveResponseEvent{" +
                "requestNo=" + requestNo +
                ", value=" + value +
                "} " + super.toString();
    }
}
