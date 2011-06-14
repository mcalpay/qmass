package org.mca.qmass.grid.request;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:23:47
 */
public class GetResponse implements Response {

    private Serializable value;

    private Integer requestNo;

    public GetResponse(Integer requestNo, Serializable value) {
        this.value = value;
        this.requestNo = requestNo;
    }

    public Serializable getValue() {
        return value;
    }

    public Integer getRequestNo() {
        return requestNo;
    }

    @Override
    public String toString() {
        return "GetResponse{" +
                "value=" + value +
                ", requestNo=" + requestNo +
                '}';
    }
}
