package org.mca.qmass.grid.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.grid.request.Response;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 09:50:35
 */
public class GetResponseEvent extends Event implements Response {

    private Serializable requestNo;

    private Serializable value;

    public GetResponseEvent(QMass qm,  Serializable serviceId, Serializable requestNo, Serializable value) {
        super(qm.getId(), serviceId, GetResponseEventHandler.class.getName());
        this.requestNo = requestNo;
        this.value = value;
    }

    public Serializable getRequestNo() {
        return requestNo;
    }

    public Serializable getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetResponseEvent that = (GetResponseEvent) o;

        if (!requestNo.equals(that.requestNo)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestNo.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GetResponse{" +
                "requestNo=" + requestNo +
                ", value=" + value +
                "}";
    }
}
