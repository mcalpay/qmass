package org.mca.qmass.grid.node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.grid.DefaultGrid;
import org.mca.qmass.grid.DefaultRequestResponseHandler;
import org.mca.qmass.grid.RequestResponseHandler;
import org.mca.qmass.grid.exception.TimeoutException;
import org.mca.qmass.grid.request.GetResponse;
import org.mca.qmass.grid.request.PutResponse;
import org.mca.qmass.grid.request.Response;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:33:58
 */
public class FarGridNode implements GridNode {

    protected final Log log = LogFactory.getLog(getClass());

    private RequestResponseHandler defaultRequestResponseHandler;

    public FarGridNode(GridNode masterGridNode, InetSocketAddress channelSocket, InetSocketAddress targetSocket) {
        this.defaultRequestResponseHandler = new DefaultRequestResponseHandler(masterGridNode,
                channelSocket,
                targetSocket);
        this.defaultRequestResponseHandler.startWork();
    }

    public Boolean put(Serializable key, Serializable value) {
        Serializable no = defaultRequestResponseHandler.sendPutRequest(key, value);
        if (DefaultGrid.getQMassGridIR().getWaitForPutResponse()) {
            PutResponse prs = (PutResponse) poll(no);
            if (prs != null) {
                return prs.isSuccessfull();
            } else {
                throw new TimeoutException("put response timed out");
            }
        }
        return Boolean.TRUE;
    }

    public Serializable get(Serializable key) {
        Serializable no = defaultRequestResponseHandler.sendGetRequest(key);
        GetResponse rh = (GetResponse) poll(no);
        if (rh != null) {
            return rh.getValue();
        } else {
            throw new TimeoutException("get response timed out");
        }
    }

    public Response poll(Serializable no) {
        Response r = null;
        long start = System.currentTimeMillis();
        long timeSpent = 0L;
        do {
            r = (GetResponse) defaultRequestResponseHandler.consumeResponse(no);
            timeSpent = System.currentTimeMillis() - start;
        } while (r == null &&
                timeSpent < DefaultGrid.getQMassGridIR().getResponseTimeout());
        log.debug("time spent waiting for response : " + timeSpent);
        return r;
    }

    @Override
    public GridNode end() {
        defaultRequestResponseHandler.endWork();
        return this;
    }

}
