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
package org.mca.qmass.core.event.greet;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.event.Event;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 17:37:51
 */
public class GreetEvent extends Event {

    private InetSocketAddress listeningAt;

    private InetSocketAddress[] cluster;

    public GreetEvent(QMass qm, Service service, InetSocketAddress listeningAt) {
        super(qm, service, GreetEventHandler.class);
        this.listeningAt = listeningAt;
        cluster = getClusterManager(qm).getCluster();
    }


    private DatagramClusterManager getClusterManager(QMass qmass) {
        return (DatagramClusterManager) qmass.getClusterManager();
    }

    public InetSocketAddress getListeningAt() {
        return listeningAt;
    }

    public InetSocketAddress[] getCluster() {
        return cluster;
    }
}