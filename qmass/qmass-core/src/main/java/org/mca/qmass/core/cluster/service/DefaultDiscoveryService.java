/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    private TCPChannelService tcpChannelService;

    private Set<InetSocketAddress> cluster;

    public DefaultDiscoveryService(TCPChannelService tcpChannelService) {
        this.tcpChannelService = tcpChannelService;
        cluster = new CopyOnWriteArraySet();
    }

    @Override
    public void addToCluster(InetSocketAddress sock) {
        tcpChannelService.getConnectedChannel(sock);
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
