package org.mca.qmass.grid.service;

import org.mca.ir.IR;
import org.mca.ir.IRKey;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.service.EventService;
import org.mca.qmass.grid.Filter;
import org.mca.qmass.grid.QMassGrid;
import org.mca.qmass.grid.event.*;
import org.mca.qmass.core.id.DefaultIdGenerator;
import org.mca.qmass.core.id.IdGenerator;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.request.Response;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 10:19:59
 */
public class DefaultGridService implements GridService {

    protected final YALog log = YALogFactory.getLog(DefaultGridService.class);

    private GridId id;

    private QMass qmass;

    private EventService manager;

    private IdGenerator idGenerator;

    private Map<Serializable, Response> responseMap = new HashMap<Serializable, Response>();

    private Map<Serializable, CountDownLatch> latchMap = new HashMap<Serializable, CountDownLatch>();

    private GridNode masterGridNode;

    private InetSocketAddress target;

    private GridId targetId;

    public DefaultGridService(QMass qmass, GridNode masterGridNode, GridId id) {
        this.id = id;
        this.qmass = qmass;
        this.manager = qmass.getEventService();
        this.masterGridNode = masterGridNode;
        this.target = id.getTarget();
        this.idGenerator = new DefaultIdGenerator();
        this.targetId = new GridId(id.getVar(), this.manager.getListening());
        this.qmass.registerService(this);
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Serializable sendPut(Serializable key, Serializable value) {
        Serializable no = getRequestNo();
        log.debug(this + " send put for : " + key + ", request : " + no);
        PutRequestEvent putRequest = new PutRequestEvent(qmass, targetId, no, key, value, getIR().getWaitForPutResponse());
        manager.sendEvent(target, putRequest);
        return no;
    }

    @Override
    public Serializable sendMerge(Serializable key, Serializable value) {
        Serializable no = getRequestNo();
        log.debug(this + " send merge for : " + key + ", request : " + no);
        MergeRequestEvent req = new MergeRequestEvent(qmass, targetId, no, key, value, getIR().getWaitForPutResponse());
        manager.sendEvent(target, req);
        return no;
    }

    private Serializable getRequestNo() {
        return this.idGenerator.nextId();
    }

    @Override
    public Serializable sendGet(Serializable key) {
        Serializable no = getRequestNo();
        log.debug(this + " send get for : " + key + ", request : " + no);
        GetRequestEvent getRequest = new GetRequestEvent(qmass, targetId, no, key);
        manager.sendEvent(target, getRequest);
        return no;
    }

    @Override
    public Serializable sendFilter(Filter filter) {
        Serializable no = getRequestNo();
        log.debug(this + " send filter : " + filter + ", request : " + no);
        FilterRequestEvent req = new FilterRequestEvent(qmass, targetId, no, filter, getIR().getWaitForPutResponse());
        manager.sendEvent(target, req);
        return no;
    }

    @Override
    public Serializable sendRemove(Serializable key) {
        Serializable no = getRequestNo();
        log.debug(this + " send remove for : " + key + ", request : " + no);
        RemoveRequestEvent getRequest = new RemoveRequestEvent(qmass, targetId, no, key,
                getIR().getWaitForRemoveResponse());
        manager.sendEvent(target, getRequest);
        return no;
    }

    @Override
    public Response consumeResponse(Serializable no) {
        Response response;
        CountDownLatch latch = null;
        synchronized (responseMap) {
            response = responseMap.remove(no);
            if (response == null) {
                latch = new CountDownLatch(1);
                latchMap.put(no, latch);
                log.debug(this + " locked " + no + " latch " + latch);
            }
        }
        if (response == null && latch != null) {
            try {
                if (latch.await(getIR().getResponseTimeout(), TimeUnit.MILLISECONDS)) {
                    synchronized (responseMap) {
                        response = responseMap.remove(no);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return response;
    }

    @Override
    public void saveResponse(Response response) {
        Serializable no = response.getRequestNo();
        CountDownLatch latch;
        synchronized (responseMap) {
            responseMap.put(no, response);
            latch = latchMap.remove(no);
            log.debug(this + " found lock " + no + " latch " + latch);
            if (latch != null) {
                latch.countDown();
                log.debug(this + " unlocking " + no);
            }
        }
    }

    @Override
    public void respondToPut(PutRequestEvent event) {
        Boolean ok = this.masterGridNode.put(event.getKey(), event.getValue());
        if (event.isWaitingForResponse()) {
            PutResponseEvent response = new PutResponseEvent(qmass, targetId, event.getRequestNo(), ok);
            log.debug(this + " send : " + response);
            manager.sendEvent(target, response);
        }
    }

    @Override
    public void respondToGet(GetRequestEvent event) {
        GetResponseEvent response = new GetResponseEvent(qmass, targetId, event.getRequestNo(),
                this.masterGridNode.get(event.getKey()));
        log.debug(this + " send : " + response);
        manager.sendEvent(target, response);
    }

    @Override
    public void respondToRemove(RemoveRequestEvent event) {
        RemoveResponseEvent response = new RemoveResponseEvent(qmass, targetId, event.getRequestNo(),
                this.masterGridNode.remove(event.getKey()));
        log.debug(this + " send : " + response);
        manager.sendEvent(target, response);
    }

    @Override
    public void respondToMerge(MergeRequestEvent event) {
        log.debug(this + " handling merge " + event);
        this.masterGridNode.merge(event.getKey(), event.getValue());
        if (event.isWaitingForResponse()) {
            MergeResponseEvent response = new MergeResponseEvent(qmass, targetId, event.getRequestNo(), true);
            log.debug(this + " send : " + response);
            manager.sendEvent(target, response);
        }
    }

    @Override
    public void respondToFilter(FilterRequestEvent event) {
        log.debug(this + " handling filter " + event);
        if (event.isWaitingForResponse()) {
            FilterResponseEvent response = new FilterResponseEvent(qmass, targetId, event.getRequestNo(),
                    (Serializable) this.masterGridNode.filter(event.getValue()));
            log.debug(this + " send : " + response);
            manager.sendEvent(target, response);
        }
    }

    private QMassGridIR getIR() {
        return IR.get(new IRKey(qmass.getId(), QMassGrid.QMASS_GRID_IR));
    }

    @Override
    public String toString() {
        return "" + id.getTarget().getPort();
    }
}
