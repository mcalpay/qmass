package org.mca.qmass.core.event.leave;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.AbstractEvent;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 14:37:50
 */
public class LeaveEvent extends AbstractEvent {

    private String hostName;

    private int port;

    public LeaveEvent(QMass qm, Service service, InetSocketAddress listeningAt) {
        super(qm, service, LeaveEventHandler.class);
        hostName = listeningAt.getHostName();
        port = listeningAt.getPort();
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }
}
