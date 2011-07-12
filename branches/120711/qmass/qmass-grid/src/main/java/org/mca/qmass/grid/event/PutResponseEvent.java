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
 * Date: 15.Haz.2011
 * Time: 09:57:45
 */
public class PutResponseEvent extends Event implements Response {

    private Serializable requestNo;

    private boolean successfull;

    public PutResponseEvent(QMass qm, Serializable serviceId,
                            Serializable requestNo, boolean successfull) {
        super(qm.getId(), serviceId, PutResponseEventHandler.class.getName());
        this.requestNo = requestNo;
        this.successfull = successfull;
    }

    public Serializable getRequestNo() {
        return requestNo;
    }

    public boolean isSuccessfull() {
        return successfull;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PutResponseEvent that = (PutResponseEvent) o;

        if (successfull != that.successfull) return false;
        if (!requestNo.equals(that.requestNo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestNo.hashCode();
        result = 31 * result + (successfull ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PutResponse{" +
                "requestNo=" + requestNo +
                ", successfull=" + successfull +
                "} " + super.toString();
    }
}
