package org.mca.qmass.grid.request;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:52:33
 */
public class PutResponse implements Response {

    private Serializable requestNo;

    private boolean successfull;

    public PutResponse(Serializable requestNo, boolean successfull) {
        this.requestNo = requestNo;
        this.successfull = successfull;
    }

    @Override
    public Serializable getRequestNo() {
        return requestNo;
    }

    public boolean isSuccessfull() {
        return successfull;
    }

    @Override
    public String toString() {
        return "PutResponse{" +
                "requestNo=" + requestNo +
                ", successfull=" + successfull +
                '}';
    }
}

