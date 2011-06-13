package org.mca.qmass.grid.request;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 10:52:33
 */
public class PutRequestResponse implements Request {

    private int requestNo;

    private boolean successfull;

    public PutRequestResponse(int requestNo, boolean successfull) {
        this.requestNo = requestNo;
        this.successfull = successfull;
    }

    @Override
    public Integer getRequestNo() {
        return requestNo;
    }

    public boolean isSuccessfull() {
        return successfull;
    }
}
