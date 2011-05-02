package org.mca.qmass.core.event.greet;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.AbstractEvent;
import org.mca.qmass.core.event.greet.GreetEventHandler;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 17:37:51
 */
public class GreetEvent extends AbstractEvent {

    private InetSocketAddress listeningAt;

    private InetSocketAddress[] cluster;

    public GreetEvent(QMass qm, Service service, InetSocketAddress listeningAt) {
        super(qm, service, GreetEventHandler.class);
        this.listeningAt = listeningAt;
        cluster = qm.getCluster();
    }

    public InetSocketAddress getListeningAt() {
        return listeningAt;
    }

    public InetSocketAddress[] getCluster() {
        return cluster;
    }
}
