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
package org.mca.qmass.grid.request;

import org.mca.qmass.grid.DefaultGrid;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:52:23
 */
public class PutRequest implements Request {

    private Serializable requestNo;

    private Serializable key;

    private Serializable value;

    private boolean waitingForResponse = false;

    public PutRequest(Serializable requestNo, Serializable key, Serializable value) {
        this.requestNo = requestNo;
        this.key = key;
        this.value = value;
        this.waitingForResponse = DefaultGrid.getQMassGridIR().getWaitForPutResponse();
    }

    public boolean isWaitingForResponse() {
        return waitingForResponse;
    }

    @Override
    public Serializable getRequestNo() {
        return requestNo;
    }

    public Serializable getKey() {
        return key;
    }

    public Serializable getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PutRequest{" +
                "requestNo=" + requestNo +
                ", key=" + key +
                ", value=" + value +
                '}';
    }
}
