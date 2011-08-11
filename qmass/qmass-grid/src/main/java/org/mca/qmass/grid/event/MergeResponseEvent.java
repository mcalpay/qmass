package org.mca.qmass.grid.event;

import org.mca.qmass.core.QMass;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 10.08.2011
 * Time: 21:22
 */
public class MergeResponseEvent extends PutResponseEvent {
    public MergeResponseEvent(QMass qm, Serializable serviceId, Serializable requestNo, boolean successfull) {
        super(qm, serviceId, requestNo, successfull, MergeResponseEventHandler.class.getName());
    }

    @Override
    public String toString() {
        return "MergeResponse{" +
                "requestNo=" + getRequestNo() +
                ", successfull=" + isSuccessfull() +
                "}";
    }
}
