package org.mca.qmass.grid.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.grid.request.Request;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 09:49:28
 */
public class GetRequestEvent extends Event implements Request {

    private Serializable requestNo;

    private Serializable key;

    public GetRequestEvent(QMass qm, Serializable serviceId, Serializable requestNo, Serializable key) {
        super(qm.getId(), serviceId, GetRequestEventHandler.class.getName());
        this.requestNo = requestNo;
        this.key = key;
    }

    public Serializable getRequestNo() {
        return requestNo;
    }

    public Serializable getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetRequestEvent that = (GetRequestEvent) o;

        if (!key.equals(that.key)) return false;
        if (!requestNo.equals(that.requestNo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestNo.hashCode();
        result = 31 * result + key.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Get{" +
                "requestNo=" + requestNo +
                ", key=" + key +
                "}";
    }
}
