package org.mca.qmass.grid.event;

import org.mca.qmass.core.QMass;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 10.08.2011
 * Time: 21:18
 */
public class MergeRequestEvent extends PutRequestEvent {

    public MergeRequestEvent(QMass qm, Serializable serviceId, Serializable requestNo, Serializable key,
                             Serializable value, boolean waitingForResponse) {
        super(qm, serviceId, requestNo, key, value, waitingForResponse,
                MergeRequestEventHandler.class.getName());
    }

    @Override
    public String toString() {
        return "Merge{" +
                "no=" + getRequestNo() +
                ", key=" + getKey() +
                ", val=" + getValue() +
                "}";
    }
}
