package org.mca.qmass.core.cluster;

import org.mca.qmass.core.event.Event;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 13.May.2011
 * Time: 13:03:47
 */
public interface P2PClusterManager {

    P2PClusterManager safeSendEvent(InetSocketAddress to, Event event);

    P2PClusterManager addToCluster(InetSocketAddress listeningAt);

    P2PClusterManager removeFromCluster(InetSocketAddress who);

    InetSocketAddress[] getCluster();
    
}
