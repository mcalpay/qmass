package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.event.Event;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * User: malpay
 * Date: 13.May.2011
 * Time: 14:03:09
 */
public abstract class AbstractP2PClusterManager implements ClusterManager {
                                                                
    protected final Log logger = LogFactory.getLog(getClass());

    protected final Set<InetSocketAddress> cluster;

    public AbstractP2PClusterManager() {
        this.cluster = new CopyOnWriteArraySet<InetSocketAddress>();
    }

    @Override
    public ClusterManager sendEvent(Event event) throws IOException {
        for (InetSocketAddress to : cluster) {
            doSendEvent(to, event);
        }
        return this;
    }

    @Override
    public ClusterManager safeSendEvent(InetSocketAddress who, Event event) {
        try {
            return doSendEvent(who, event);
        } catch (IOException e) {
            logger.error(getId() + " had error trying to send event", e);
        }
        return this;
    }

    protected ClusterManager doAddToCluster(InetSocketAddress who) throws IOException {
        return this;
    }

    protected abstract ClusterManager doSendEvent(InetSocketAddress who, Event event) throws IOException;

    @Override
    public ClusterManager addToCluster(InetSocketAddress who) {
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
    public ClusterManager removeFromCluster(InetSocketAddress who) {
        if (cluster.contains(who)) {
            cluster.remove(who);
            try {
                doRemoveFromCluster(who);
            } catch (IOException e) {
                logger.error("Error removing : " + who, e);
            }
        }
        logger.info("Cluster;\n\t" + getId() + "\n\t" + cluster);
        return this;
    }

    protected ClusterManager doRemoveFromCluster(InetSocketAddress who) throws IOException {
        return this;
    }

    @Override
    public InetSocketAddress[] getCluster() {
        return cluster.toArray(new InetSocketAddress[cluster.size()]);
    }


}
