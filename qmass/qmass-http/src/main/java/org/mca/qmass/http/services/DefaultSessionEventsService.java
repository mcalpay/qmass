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
package org.mca.qmass.http.services;

import org.mca.qmass.core.QMass;
import org.mca.qmass.http.events.AttributeAddEvent;
import org.mca.qmass.http.events.AttributeRemoveEvent;
import org.mca.qmass.http.events.BindingEvent;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:16:57
 * <p/>
 * Default implemenation of the SessionEventsService @see SessionEventsService
 */
public class DefaultSessionEventsService implements SessionEventsService {

    private Serializable id;

    private QMass qmass;

    private Map attributes = new HashMap();

    private Map trackedHashes = new HashMap<String, Object>();

    public DefaultSessionEventsService(String id, QMass qmass) {
        this.id = id;
        this.qmass = qmass;
        this.qmass.registerService(this);
    }

    public Serializable getId() {
        return id;
    }

    @Override
    public SessionEventsService attributeAdded(String name, Object value) {
        qmass.sendEvent(new AttributeAddEvent(qmass, this, name, (Serializable) value));
        trackedHashes.put(name, (value != null) ? value.hashCode() : null);
        return this;
    }

    @Override
    public SessionEventsService attributeRemoved(String name) {
        qmass.sendEvent(new AttributeRemoveEvent(qmass, this, name));
        trackedHashes.remove(name);
        return this;
    }

    @Override
    public SessionEventsService end() {
        this.qmass.unRegisterService(this);
        attributes = null;
        return this;
    }

    @Override
    public SessionEventsService checkForChangedAttributes(HttpSession session) {
        for (Object name : trackedHashes.keySet()) {
            Object attribute = session.getAttribute((String) name);
            Integer hash = (attribute != null) ? attribute.hashCode() : null;
            Object trackedHash = trackedHashes.get(name);
            if (trackedHash == null || !trackedHash.equals(hash)) {
                attributeAdded((String) name, session.getAttribute((String) name));
            }
        }
        return this;
    }

    @Override
    public SessionEventsService doAttributeAdded(Serializable name, Serializable value) {
        attributes.put(name, value);
        trackedHashes.put(name, (value != null) ? value.hashCode() : null);
        return this;
    }

    @Override
    public SessionEventsService doAttributeRemoved(Serializable name) {
        attributes.remove(name);
        trackedHashes.remove(name);
        return this;
    }

    @Override
    public SessionEventsService sync(HttpSession session) {
        for (Object name : attributes.keySet()) {
            session.setAttribute((String) name, attributes.get(name));
        }
        attributes.clear();
        return this;
    }
}
