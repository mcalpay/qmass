package org.mca.qmass.grid;

import org.mca.ir.IR;
import org.mca.qmass.grid.exception.TimeoutException;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.request.GetRequestResponse;
import org.mca.qmass.grid.request.PutRequestResponse;
import org.mca.qmass.grid.request.Request;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:33:58
 */
public class FarGridMap implements GridMap {

    private RequestResponseHandler defaultRequestResponseHandler;

    public FarGridMap(GridMap masterGridMap, InetSocketAddress channelSocket, InetSocketAddress targetSocket) {
        this.defaultRequestResponseHandler = new DefaultRequestResponseHandler(masterGridMap,
                channelSocket,
                targetSocket);
        this.defaultRequestResponseHandler.startWork();
    }

    public Boolean put(Serializable key, Serializable value) {
        int no = defaultRequestResponseHandler.sendPutRequest(key, value);
        if (getQMassGridIR().getWaitForPutResponse()) {
            PutRequestResponse prs = (PutRequestResponse) poll(no);
            if (prs != null) {
                return prs.isSuccessfull();
            } else {
                throw new TimeoutException("put response timed out");
            }
        }
        return Boolean.TRUE;
    }

    public Serializable get(Serializable key) {
        int no = defaultRequestResponseHandler.sendGetRequest(key);
        GetRequestResponse rh = (GetRequestResponse) poll(no);
        if (rh != null) {
            return rh.getValue();
        } else {
            throw new TimeoutException("get response timed out");
        }
    }

    public Request poll(int no) {
        Request r = null;
        long start = System.currentTimeMillis();
        do {
            r = (GetRequestResponse) defaultRequestResponseHandler.response(no);
        } while (r == null &&
                System.currentTimeMillis() - start < getQMassGridIR().getResponseTimeout());
        return r;
    }

    private QMassGridIR getQMassGridIR() {
        return IR.get("default", "QMassGridIR");
    }

    @Override
    public GridMap end() {
        defaultRequestResponseHandler.endWork();
        return this;
    }

}
