package org.mca.qmass.core.cluster;

import org.mca.qmass.core.event.Event;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 15:56:13
 */
public interface ClusterManager {

    ClusterManager sendEvent(Event event);

    ClusterManager handleEvent();

    ClusterManager end();

    ClusterManager start();
}
