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
package org.mca.qmass.core.event.leave;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.DatagramClusterManager;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 14:46:41
 */
public class DefaultLeaveService implements LeaveService {

    private Serializable id;

    private QMass qmass;

    private InetSocketAddress listeningAt;

    private List<NodeLeaveListener> listeners = new ArrayList<NodeLeaveListener>();

    @Override
    public Serializable getId() {
        return id;
    }

    public DefaultLeaveService(QMass qmass, InetSocketAddress listeningAt) {
        this.id = qmass.getId() + "/Leave";
        this.qmass = qmass;
        this.listeningAt = listeningAt;
        this.qmass.registerService(this);
    }

    @Override
    public DefaultLeaveService removeFromCluster(InetSocketAddress who) {
        getClusterManager().removeFromCluster(who);
        for (NodeLeaveListener listener : listeners) {
            listener.leave(who);
        }
        return this;
    }

    private DatagramClusterManager getClusterManager() {
        return (DatagramClusterManager) qmass.getClusterManager();
    }

    @Override
    public DefaultLeaveService leave() {
        qmass.sendEvent(new LeaveEvent(qmass, this, listeningAt));
        return this;
    }

    @Override
    public DefaultLeaveService registerNodeLeaveListener(NodeLeaveListener listener) {
        listeners.add(listener);
        return this;
    }
}
