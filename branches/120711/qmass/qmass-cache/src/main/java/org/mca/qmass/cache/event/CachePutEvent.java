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
package org.mca.qmass.cache.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 29.Nis.2011
 * Time: 15:00:08
 */
public class CachePutEvent extends Event {

    private Serializable key;

    private Serializable value;

    public CachePutEvent(QMass qm, Service service,
                         Serializable key, Serializable value) {
        super(qm, service, CachePutEventHandler.class);
        this.key = key;
        this.value = value;
    }

    public Serializable getKey() {
        return key;
    }

    public Serializable getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CachePutEvent{" +
                "key=" + key +
                "} " + super.toString();
    }
}
