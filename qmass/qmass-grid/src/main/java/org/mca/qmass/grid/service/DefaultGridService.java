package org.mca.qmass.grid.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.cluster.P2PClusterManager;
import org.mca.qmass.grid.event.GetRequestEvent;
import org.mca.qmass.grid.event.GetResponseEvent;
import org.mca.qmass.grid.event.PutRequestEvent;
import org.mca.qmass.grid.event.PutResponseEvent;
import org.mca.qmass.grid.id.DefaultIdGenerator;
import org.mca.qmass.grid.id.IdGenerator;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.request.Response;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 10:19:59
 */
public class DefaultGridService implements GridService {

    protected final Log log = LogFactory.getLog(getClass());

    private Serializable id;

    private QMass qmass;

    private DatagramClusterManager manager;

    private IdGenerator idGenerator;

    private Map<Serializable, Response> responseMap = new ConcurrentHashMap<Serializable, Response>();

    private GridNode masterGridNode;

    private InetSocketAddress target;

    private Serializable targetService;

    public DefaultGridService(Serializable id, QMass qmass, GridNode masterGridNode, InetSocketAddress target) {
        this.id = id;
        this.qmass = qmass;
        this.manager = (DatagramClusterManager) qmass.getClusterManager();
        this.masterGridNode = masterGridNode;
        this.target = target;
        this.idGenerator = new DefaultIdGenerator(target);
        this.targetService = this.manager.getListeningAt();
        this.qmass.registerService(this);
    }

    public DefaultGridService(QMass qmass, GridNode masterGridNode, InetSocketAddress target) {
        this.id = target;
        this.qmass = qmass;
        this.manager = (DatagramClusterManager) qmass.getClusterManager();
        this.masterGridNode = masterGridNode;
        this.target = target;
        this.idGenerator = new DefaultIdGenerator(target);
        this.targetService = this.manager.getListeningAt();
        this.qmass.registerService(this);
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Serializable sendPut(Serializable key, Serializable value) {
        log.debug(this + " send put for : " + key + ", " + value);
        Serializable no = getRequestNo();
        PutRequestEvent putRequest = new PutRequestEvent(qmass, targetService, no, key, value);
        manager.safeSendEvent(target, putRequest);
        return no;
    }

    private Serializable getRequestNo() {
        return this.idGenerator.nextId();
    }

    @Override
    public Serializable sendGet(Serializable key) {
        log.debug(this + " send get for : " + key);
        Serializable no = getRequestNo();
        GetRequestEvent getRequest = new GetRequestEvent(qmass, targetService, no, key);
        manager.safeSendEvent(target, getRequest);
        return no;
    }

    @Override
    public GridService saveResponse(Response response) {
        log.debug(this + " response : " + response);
        responseMap.put(response.getRequestNo(), response);
        return this;
    }

    @Override
    public GridService respondToPut(PutRequestEvent event) {
        Boolean ok = this.masterGridNode.put(event.getKey(), event.getValue());
        if (event.isWaitingForResponse()) {
            PutResponseEvent response = new PutResponseEvent(qmass, targetService, event.getRequestNo(), ok);
            log.debug(this + " send : " + response);
            manager.safeSendEvent(target, response);
        }
        return this;
    }

    @Override
    public GridService respondToGet(GetRequestEvent event) {
        GetResponseEvent response = new GetResponseEvent(qmass, targetService, event.getRequestNo(),
                this.masterGridNode.get(event.getKey()));
        log.debug(this + " send : " + response);
        manager.safeSendEvent(target, response);
        return this;
    }

    @Override
    public Response consumeResponse(Serializable no) {
        log.trace(this + " consuming response " + no);
        return responseMap.remove(no);
    }

}
