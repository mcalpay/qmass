/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mca.qmass.grid;

import org.mca.ir.IR;
import org.mca.ir.IRKey;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.greet.NodeGreetListener;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.event.leave.NodeLeaveListener;
import org.mca.qmass.grid.ir.DefaultQMassGridIR;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.node.LocalGridNode;
import org.mca.qmass.grid.node.QMassGridNode;
import org.mca.qmass.persistence.*;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 14:37:17
 */
public class QMassGrid extends DefaultGrid
        implements NodeLeaveListener, NodeGreetListener, Service {

    private Serializable id;

    private Serializable var;

    private PersistenceService persistenceService;

    private QMassGridIR ir;

    public static final String QMASS_GRID_IR = "QMassGridIR";

    public QMassGrid(Serializable var, QMass qmass) {
        super(new LocalGridNode(qmass.getEventService().getListening()),
                qmass);
        this.var = var;
        this.id = QMassGrid.class + "/" + var.toString();
        IR.putIfDoesNotContain(new IRKey(qmass.getId(), QMASS_GRID_IR), DefaultQMassGridIR.instance());
        ir = IR.get(new IRKey(qmass.getId(), QMASS_GRID_IR));
        this.qmass.registerService(this);
        GreetService greetService = (GreetService) qmass.getService(GreetService.class);
        greetService.registerNodeWelcomeListener(this);
        LeaveService leaveService = (LeaveService) qmass.getService(LeaveService.class);
        leaveService.registerNodeLeaveListener(this);
        if (ir.persists()) {
            persistenceService = new QueuedPersistenceService(var.toString());
        } else {
            persistenceService = new NOOPPersistenceService();
        }

    }

    public QMassGrid(QMass qmass) {
        this("default", qmass);
    }

    public Serializable getId() {
        return id;
    }

    @Override
    public void merge(Serializable key, Serializable value) {
        super.merge(key, value);
    }

    @Override
    public Boolean put(Serializable key, Serializable value) {
        persistenceService.persist(key, value);
        return super.put(key, value);
    }

    @Override
    public Serializable get(Serializable key) {
        try {
            Serializable serializable = super.get(key);
            if (serializable != null) {
                return serializable;
            }

        } catch (Exception e) {
            log.info("error trying to get on mem", e);
        }

        Serializable val = persistenceService.get(key);
        if (val != null) {
            super.put(key, val);
        }

        return val;
    }

    @Override
    public Serializable remove(Serializable key) {
        persistenceService.remove(key);
        return super.remove(key);
    }

    @Override
    public void greet(InetSocketAddress who) {
        addGridNode(new QMassGridNode(var, qmass, masterGridNode, who));
    }

    @Override
    public NodeLeaveListener leave(InetSocketAddress who) {
        removeGridNode(findNodeWithSocket(who));
        return this;
    }

    @Override
    public void end() {
        persistenceService.end();
        super.end();
    }
}
