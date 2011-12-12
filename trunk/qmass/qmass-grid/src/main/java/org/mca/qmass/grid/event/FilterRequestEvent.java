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
import org.mca.qmass.grid.service.GridId;
import org.mca.qmass.persistence.FilterPredicate;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 02.09.2011
 * Time: 12:28
 */
public class FilterRequestEvent extends PutRequestEvent {

    public FilterRequestEvent(QMass qmass, GridId targetId, Serializable no, FilterPredicate filter,
                              boolean waitForPutResponse) {
        super(qmass, targetId, no, null, filter, waitForPutResponse,FilterRequestEventHandler.class.getName());
    }

    @Override
    public FilterPredicate getValue() {
        return (FilterPredicate) super.getValue();
    }

    @Override
    public String toString() {
        return "FilterRequestEvent{" +
                "no=" + getRequestNo() +
                ", val=" + getValue() +
                "}";
    }
}
