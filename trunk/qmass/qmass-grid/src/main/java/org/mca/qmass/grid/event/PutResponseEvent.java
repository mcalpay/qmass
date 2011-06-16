package org.mca.qmass.grid.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.grid.request.Response;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 09:57:45
 */
public class PutResponseEvent extends Event implements Response {

    private Serializable requestNo;

    private boolean successfull;

    public PutResponseEvent(QMass qm, Serializable serviceId,
                            Serializable requestNo, boolean successfull) {
        super(qm.getId(), serviceId, PutResponseEventHandler.class.getName());
        this.requestNo = requestNo;
        this.successfull = successfull;
    }

    public Serializable getRequestNo() {
        return requestNo;
    }

    public boolean isSuccessfull() {
        return successfull;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PutResponseEvent that = (PutResponseEvent) o;

        if (successfull != that.successfull) return false;
        if (!requestNo.equals(that.requestNo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestNo.hashCode();
        result = 31 * result + (successfull ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PutResponse{" +
                "requestNo=" + requestNo +
                ", successfull=" + successfull +
                "} " + super.toString();
    }
}
