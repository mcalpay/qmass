package org.mca.qmass.core.cluster.service;

import org.mca.qmass.core.Service;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 27.Tem.2011
 * Time: 15:14:05
 */
public interface DiscoveryService {

    void addToCluster(InetSocketAddress listeningAt);

    void removeFromCluster(InetSocketAddress who);

    InetSocketAddress[] getCluster();

}
