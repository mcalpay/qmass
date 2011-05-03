package org.mca.qmass.cache.event;

import org.mca.qmass.cache.ReplicatedQCache;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.AbstractEvent;
import org.mca.qmass.core.event.Event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 29.Nis.2011
 * Time: 15:00:08
 */
public class CachePutEvent extends AbstractEvent {

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
                ", value=" + value +
                "} " + super.toString();
    }
}
