package org.mca.qmass.core.event.greet;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.event.QMassEvent;
import org.mca.qmass.core.event.greet.GreetEventHandler;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 17:37:51
 */
public class GreetEvent extends QMassEvent {

    private InetSocketAddress listeningAt;

    private InetSocketAddress[] cluster;

    public GreetEvent(QMass qm, Service service, InetSocketAddress listeningAt) {
        super(qm, service, GreetEventHandler.class);
        this.listeningAt = listeningAt;
        cluster = getClusterManager(qm).getCluster();
    }


    private DatagramClusterManager getClusterManager(QMass qmass) {
        return (DatagramClusterManager) qmass.getClusterManager();
    }

    public InetSocketAddress getListeningAt() {
        return listeningAt;
    }

    public InetSocketAddress[] getCluster() {
        return cluster;
    }
}
