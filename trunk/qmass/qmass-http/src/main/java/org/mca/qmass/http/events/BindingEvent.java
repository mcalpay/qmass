package org.mca.qmass.http.events;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:39:55
 */
public abstract class BindingEvent extends Event {

    private Serializable name;

    private Serializable value;

    public BindingEvent(QMass qm, Service service,
                        Serializable name, Serializable value) {
        super(qm, service, BindingEventHandler.class);
        this.name = name;
        this.value = value;
    }

    public Serializable getName() {
        return name;
    }

    public Serializable getValue() {
        return value;
    }
}
