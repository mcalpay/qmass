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

    public GreetEvent(QMass qm, Service service, InetSocketAddress listeningAt) {
        super(qm, service, GreetEventHandler.class);
        appendSocket(listeningAt);
        InetSocketAddress [] cluster = qm.getCluster();
        for (InetSocketAddress s : cluster) {
            appendSocket(s);
        }
    }

    private void appendSocket(InetSocketAddress listeningAt) {
        append("(").append(listeningAt.getHostName())
                .append(",").append(Integer.toString(listeningAt.getPort())).append(")");
    }

}
