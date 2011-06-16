package org.mca.qmass.grid.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.grid.DefaultGrid;
import org.mca.qmass.grid.request.Request;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 09:56:42
 */
public class PutRequestEvent extends Event implements Request {

    private Serializable requestNo;

    private Serializable key;

    private Serializable value;

    private boolean waitingForResponse = false;

    public PutRequestEvent(QMass qm,  Serializable serviceId, Serializable requestNo,
                           Serializable key, Serializable value) {
        super(qm.getId(), serviceId, PutRequestEventHandler.class.getName());
        this.requestNo = requestNo;
        this.key = key;
        this.value = value;
        this.waitingForResponse = DefaultGrid.getQMassGridIR().getWaitForPutResponse();
    }

    public Serializable getRequestNo() {
        return requestNo;
    }

    public Serializable getKey() {
        return key;
    }

    public Serializable getValue() {
        return value;
    }

    public boolean isWaitingForResponse() {
        return waitingForResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PutRequestEvent that = (PutRequestEvent) o;

        if (!key.equals(that.key)) return false;
        if (!requestNo.equals(that.requestNo)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestNo.hashCode();
        result = 31 * result + key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PutRequest{" +
                "requestNo=" + requestNo +
                ", key=" + key +
                ", value=" + value +
                "} " + super.toString();
    }
}
