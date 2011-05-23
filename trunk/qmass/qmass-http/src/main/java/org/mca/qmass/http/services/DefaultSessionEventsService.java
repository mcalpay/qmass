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
 */
public class DefaultSessionEventsService implements SessionEventsService {

    private Serializable id;

    private QMass qmass;

    private Map attributes = new HashMap();

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
        return this;
    }

    @Override
    public SessionEventsService attributeRemoved(String name) {
        qmass.sendEvent(new AttributeRemoveEvent(qmass, this, name));
        return this;
    }

    @Override
    public SessionEventsService doAttributeAdded(Serializable name, Serializable value) {
        attributes.put(name, value);
        return this;
    }

    @Override
    public SessionEventsService doAttributeRemoved(Serializable name) {
        attributes.remove(name);
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
