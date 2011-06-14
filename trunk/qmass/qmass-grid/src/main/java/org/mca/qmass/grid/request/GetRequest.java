package org.mca.qmass.grid.request;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:33:27
 */
public class GetRequest implements Request {

    private Serializable requestNo;

    private Serializable key;

    public GetRequest(Serializable requestNo, Serializable key) {
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
    public String toString() {
        return "GetRequest{" +
                "requestNo=" + requestNo +
                ", key=" + key +
                '}';
    }
}
