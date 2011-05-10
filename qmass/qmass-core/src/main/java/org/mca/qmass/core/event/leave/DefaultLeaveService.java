package org.mca.qmass.core.event.leave;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.scanner.Scanner;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 14:46:41
 */
public class DefaultLeaveService implements LeaveService {

    private Serializable id;

    private QMass qmass;

    private InetSocketAddress listeningAt;

    @Override
    public Serializable getId() {
        return id;
    }

    public DefaultLeaveService(QMass qmass, InetSocketAddress listeningAt) {
        this.id = qmass.getId() + "leave";
        this.qmass = qmass;
        this.listeningAt = listeningAt;
        this.qmass.registerService(this);
    }

    @Override
    public DefaultLeaveService removeFromCluster(InetSocketAddress who) {
        getClusterManager().removeFromCluster(who);
        return this;
    }

    private DatagramClusterManager getClusterManager() {
        return (DatagramClusterManager) qmass.getClusterManager();
    }

    @Override
    public DefaultLeaveService leave() {
        qmass.sendEvent(new LeaveEvent(qmass, this, listeningAt));
        return this;
    }
}
