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
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.node.LocalGridNode;
import org.mca.qmass.grid.node.QMassGridNode;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 14:37:17
 */
public class QMassGrid extends DefaultGrid
        implements NodeLeaveListener, NodeGreetListener, Service {

    private Serializable id;

    private Serializable var;

    public static final String QMASS_GRID_IR = "QMassGridIR";

    public QMassGrid(Serializable var, QMass qmass) {
        super(new LocalGridNode(qmass.getClusterManager().getListeningAt()),
                qmass);
        this.var = var;
        this.id = qmass.getId() + "/Grid/" + var.toString();
        IR.putIfDoesNotContain(new IRKey(qmass.getId(), QMASS_GRID_IR), DefaultQMassGridIR.instance());
        this.qmass.registerService(this);
        GreetService greetService = (GreetService) qmass.getService(qmass.getId() + "/Greet");
        greetService.registerNodeWelcomeListener(this);
        LeaveService leaveService = (LeaveService) qmass.getService(qmass.getId() + "/Leave");
        leaveService.registerNodeLeaveListener(this);
    }

    public QMassGrid(QMass qmass) {
        super(new LocalGridNode(qmass.getClusterManager().getListeningAt()),
                qmass);
        this.var = "default";
        this.id = qmass.getId() + "/Grid";
        IR.putIfDoesNotContain(new IRKey(qmass.getId(), QMASS_GRID_IR), DefaultQMassGridIR.instance());
        this.qmass.registerService(this);
        GreetService greetService = (GreetService) qmass.getService(qmass.getId() + "/Greet");
        greetService.registerNodeWelcomeListener(this);
        LeaveService leaveService = (LeaveService) qmass.getService(qmass.getId() + "/Leave");
        leaveService.registerNodeLeaveListener(this);
    }

    public Serializable getId() {
        return id;
    }

    @Override
    public NodeGreetListener greet(InetSocketAddress who) {
        addGridNode(new QMassGridNode(var, qmass, masterGridNode, who));
        return this;
    }

    @Override
    public NodeLeaveListener leave(InetSocketAddress who) {
        removeGridNode(findNodeWithSocket(who));
        return this;
    }

    @Override
    public GridNode end() {
        this.qmass.unRegisterService(this);
        super.end();
        return this;
    }
}
