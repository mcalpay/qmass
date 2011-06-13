package org.mca.qmass.grid;

import org.mca.ir.IR;
import org.mca.qmass.grid.exception.TimeoutException;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.request.GetRequestResponse;
import org.mca.qmass.grid.request.PutRequestResponse;
import org.mca.qmass.grid.request.Request;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:33:58
 */
public class FarGrid implements Grid {

    private DatagramChannel channel;

    private InetSocketAddress targetSocket;

    private Worker worker;

    //@TODO Move these to worker...
    public FarGrid(Grid masterGrid, InetSocketAddress channelSocket, InetSocketAddress targetSocket) {
        try {
            this.channel = DatagramChannel.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.channel.socket().bind(channelSocket);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        this.targetSocket = targetSocket;
        this.worker = new Worker(masterGrid, this.channel, targetSocket);
        this.worker.start();
    }

    public Boolean put(Serializable key, Serializable value) {
        int no = worker.sendPutRequest(key, value);
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
        int no = worker.sendGetRequest(key);
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
            r = (GetRequestResponse) worker.response(no);
        } while (r == null &&
                System.currentTimeMillis() - start < getQMassGridIR().getResponseTimeout());
        return r;
    }

    private QMassGridIR getQMassGridIR() {
        return IR.get("default", "QMassGridIR");
    }

    @Override
    public Grid end() {
        worker.end();
        return this;
    }

}
