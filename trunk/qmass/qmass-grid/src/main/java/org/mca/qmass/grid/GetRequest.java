package org.mca.qmass.grid;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:33:27
 */
public class GetRequest implements Request {

    private int requestNo;

    private Serializable key;

    public GetRequest(int requestNo, Serializable key) {
        this.requestNo = requestNo;
        this.key = key;
    }

    public Integer getRequestNo() {
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
