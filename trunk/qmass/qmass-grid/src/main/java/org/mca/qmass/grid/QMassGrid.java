package org.mca.qmass.grid;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.greet.NodeGreetListener;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.event.leave.NodeLeaveListener;
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

    private QMass qmass;

    public QMassGrid(QMass qmass) {
        super(new LocalGridNode(((DatagramClusterManager) qmass.getClusterManager()).getListeningAt()));
        this.id = qmass.getId() + "/Grid";
        this.qmass = qmass;
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
        addGridNode(new QMassGridNode(qmass, masterGridNode, who));
        return this;
    }

    @Override
    public NodeLeaveListener leave(InetSocketAddress who) {
        //@TODO removeGridNode(who);
        return this;
    }
}
