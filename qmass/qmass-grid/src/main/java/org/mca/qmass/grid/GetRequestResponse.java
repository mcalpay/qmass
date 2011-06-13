package org.mca.qmass.grid;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:23:47
 */
public class GetRequestResponse implements Request {

    private Serializable value;

    private Integer requestNo;

    public GetRequestResponse(Serializable value, Integer requestNo) {
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
        return "GetRequestResponse{" +
                "value=" + value +
                ", requestNo=" + requestNo +
                '}';
    }
}
