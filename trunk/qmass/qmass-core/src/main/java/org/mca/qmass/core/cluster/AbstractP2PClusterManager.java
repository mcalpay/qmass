package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.event.Event;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * User: malpay
 * Date: 13.May.2011
 * Time: 14:03:09
 */
public abstract class AbstractP2PClusterManager implements P2PClusterManager {

    protected final Log logger = LogFactory.getLog(getClass());

    protected final Set<InetSocketAddress> cluster;

    public AbstractP2PClusterManager() {
        this.cluster = new HashSet<InetSocketAddress>();
    }

    @Override
    public P2PClusterManager safeSendEvent(InetSocketAddress who, Event event) {
        try {
            return doSendEvent(who, event);
        } catch (IOException e) {
            logger.error(getId() + " had error trying to send event", e);
        }
        return this;
    }

    protected P2PClusterManager doAddToCluster(InetSocketAddress who) throws IOException {
        return this;
    }

    protected abstract P2PClusterManager doSendEvent(InetSocketAddress who, Event event) throws IOException;

    protected abstract Serializable getId();

    @Override
    public P2PClusterManager addToCluster(InetSocketAddress who) {
        try {
            if (!cluster.contains(who)) {
                cluster.add(who);
                doAddToCluster(who);
            }
        } catch (IOException e) {
            logger.info("Error trying to add : " + who + " to cluster.", e);
        }
        logger.info("Cluster;\n\t" + getId() + "\n\t" + cluster);
        return this;
    }

    @Override
    public P2PClusterManager removeFromCluster(InetSocketAddress who) {
        if (cluster.contains(who)) {
            cluster.remove(who);
            doRemoveFromCluster(who);
        }
        logger.info("Cluster;\n\t" + getId() + "\n\t" + cluster);
        return this;
    }

    protected P2PClusterManager doRemoveFromCluster(InetSocketAddress who) {
        return this;
    }

    @Override
    public InetSocketAddress[] getCluster() {
        return cluster.toArray(new InetSocketAddress[cluster.size()]);
    }
}
