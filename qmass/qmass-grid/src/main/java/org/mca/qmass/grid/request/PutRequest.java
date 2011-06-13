package org.mca.qmass.grid.request;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:52:23
 */
public class PutRequest implements Request {

    private int requestNo;

    private Serializable key;

    private Serializable value;

    public PutRequest(int requestNo, Serializable key, Serializable value) {
        this.requestNo = requestNo;
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer getRequestNo() {
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
