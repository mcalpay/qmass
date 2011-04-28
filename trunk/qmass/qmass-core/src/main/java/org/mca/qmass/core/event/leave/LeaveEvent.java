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
    public LeaveEvent(QMass qm, Service service, InetSocketAddress listeningAt) {
        super(qm, service, LeaveEventHandler.class);
        append("(").append(listeningAt.getHostName())
                .append(",").append(listeningAt.getPort()).append(")");
    }
}
