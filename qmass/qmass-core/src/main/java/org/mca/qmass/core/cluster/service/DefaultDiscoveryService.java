package org.mca.qmass.core.cluster.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.greet.DefaultGreetService;
import org.mca.qmass.core.event.greet.GreetService;
import org.mca.qmass.core.event.leave.DefaultLeaveService;
import org.mca.qmass.core.event.leave.LeaveService;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * User: malpay
 * Date: 28.Tem.2011
 * Time: 19:28:43
 */
public class DefaultDiscoveryService implements DiscoveryService {

    private final static Log logger = LogFactory.getLog(DefaultDiscoveryService.class);

    private ChannelService channelService;

    private Set<InetSocketAddress> cluster;

    public DefaultDiscoveryService() {
        cluster = new CopyOnWriteArraySet();
    }

    @Override
    public void addToCluster(InetSocketAddress sock) {
        cluster.add(sock);
        logger.info("Cluster;\n\t" + cluster);
    }

    @Override
    public void removeFromCluster(InetSocketAddress who) {
        cluster.remove(who);
        logger.info("Cluster;\n\t" + cluster);
    }

    @Override
    public InetSocketAddress[] getCluster() {
        return cluster.toArray(new InetSocketAddress[cluster.size()]);
    }
}
