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

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:52:33
 */
public class PutResponse implements Response {

    private Serializable requestNo;

    private boolean successfull;

    public PutResponse(Serializable requestNo, boolean successfull) {
        this.requestNo = requestNo;
        this.successfull = successfull;
    }

    @Override
    public Serializable getRequestNo() {
        return requestNo;
    }

    public boolean isSuccessfull() {
        return successfull;
    }

    @Override
    public String toString() {
        return "PutResponse{" +
                "requestNo=" + requestNo +
                ", successfull=" + successfull +
                '}';
    }
}

