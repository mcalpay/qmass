package org.mca.qmass.grid.request;

import org.mca.qmass.grid.DefaultGrid;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:52:23
 */
public class PutRequest implements Request {

    private Serializable requestNo;

    private Serializable key;

    private Serializable value;

    private boolean waitingForResponse = false;

    public PutRequest(Serializable requestNo, Serializable key, Serializable value) {
        this.requestNo = requestNo;
        this.key = key;
        this.value = value;
        this.waitingForResponse = DefaultGrid.getQMassGridIR().getWaitForPutResponse();
    }

    public boolean isWaitingForResponse() {
        return waitingForResponse;
    }

    @Override
    public Serializable getRequestNo() {
        return requestNo;
    }

    public Serializable getKey() {
        return key;
    }

    public Serializable getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PutRequest{" +
                "requestNo=" + requestNo +
                ", key=" + key +
                ", value=" + value +
                '}';
    }
}
