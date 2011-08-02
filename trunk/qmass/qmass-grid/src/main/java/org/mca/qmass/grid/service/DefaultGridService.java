package org.mca.qmass.grid.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.ir.IR;
import org.mca.ir.IRKey;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.ClusterManager;
import org.mca.qmass.grid.QMassGrid;
import org.mca.qmass.grid.event.GetRequestEvent;
import org.mca.qmass.grid.event.GetResponseEvent;
import org.mca.qmass.grid.event.PutRequestEvent;
import org.mca.qmass.grid.event.PutResponseEvent;
import org.mca.qmass.grid.event.RemoveRequestEvent;
import org.mca.qmass.grid.event.RemoveResponseEvent;
import org.mca.qmass.core.id.DefaultIdGenerator;
import org.mca.qmass.core.id.IdGenerator;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.request.Response;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 10:19:59
 */
public class DefaultGridService implements GridService {

    protected final Log log = LogFactory.getLog(getClass());

    private Serializable id;

    private QMass qmass;

    private ClusterManager manager;

    private IdGenerator idGenerator;

    private Map<Serializable, Response> responseMap = new ConcurrentHashMap<Serializable, Response>();

    private Map<Serializable, CountDownLatch> latchMap = new ConcurrentHashMap<Serializable, CountDownLatch>();

    private GridNode masterGridNode;

    private InetSocketAddress target;

    private GridId targetId;

    public DefaultGridService(QMass qmass, GridNode masterGridNode, GridId id) {
        this.id = id;
        this.qmass = qmass;
        this.manager = qmass.getClusterManager();
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
        CountDownLatch latch;

        synchronized (this) {
            response = responseMap.remove(no);
            latch = new CountDownLatch(1);
            latchMap.put(no, latch);
        }
        if (response == null) {
            try {
                latch.await();
                response = responseMap.remove(no);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            latchMap.remove(no);
        }

        return response;
    }

    @Override
    public GridService saveResponse(Response response) {
        Serializable no = response.getRequestNo();
        CountDownLatch latch;

        synchronized (this) {
            responseMap.put(no, response);
            latch = latchMap.remove(no);
        }

        if (latch != null) {
            latch.countDown();
        }
        return this;
    }

    @Override
    public GridService respondToPut(PutRequestEvent event) {
        Boolean ok = this.masterGridNode.put(event.getKey(), event.getValue());
        if (event.isWaitingForResponse()) {
            PutResponseEvent response = new PutResponseEvent(qmass, targetId, event.getRequestNo(), ok);
            log.debug(this + " send : " + response);
            manager.sendEvent(target, response);
        }
        return this;
    }

    @Override
    public GridService respondToGet(GetRequestEvent event) {
        GetResponseEvent response = new GetResponseEvent(qmass, targetId, event.getRequestNo(),
                this.masterGridNode.get(event.getKey()));
        log.debug(this + " send : " + response);
        manager.sendEvent(target, response);
        return this;
    }

    @Override
    public GridService respondToRemove(RemoveRequestEvent event) {
        RemoveResponseEvent response = new RemoveResponseEvent(qmass, targetId, event.getRequestNo(),
                this.masterGridNode.remove(event.getKey()));
        log.debug(this + " send : " + response);
        manager.sendEvent(target, response);
        return this;
    }

    private QMassGridIR getIR() {
        return IR.get(new IRKey(qmass.getId(), QMassGrid.QMASS_GRID_IR));
    }

    @Override
    public String toString() {
        return "DefaultGridService{" +
                "id=" + id +
                '}';
    }
}
