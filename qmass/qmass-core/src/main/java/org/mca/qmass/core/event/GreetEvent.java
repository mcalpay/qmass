package org.mca.qmass.core.event;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 17:37:51
 */
public class GreetEvent extends AbstractEvent {

    public GreetEvent(Serializable id, InetSocketAddress listeningAt, Collection<InetSocketAddress> cluster) {
        super(id, GreetEventHandler.class);
        appendSocket(listeningAt);
        for (InetSocketAddress s : cluster) {
            appendSocket(s);
        }
    }

    private void appendSocket(InetSocketAddress listeningAt) {
        append("(").append(listeningAt.getHostName())
                .append(",").append(listeningAt.getPort()).append(")");
    }

}
