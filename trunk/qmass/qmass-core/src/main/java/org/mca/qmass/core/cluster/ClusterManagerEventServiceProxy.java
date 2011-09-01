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
package org.mca.qmass.core.cluster;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.service.EventService;
import org.mca.qmass.core.cluster.service.TCPEventService;
import org.mca.qmass.core.cluster.service.UDPEventService;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 02.Agu.2011
 * Time: 15:54:52
 */
public class ClusterManagerEventServiceProxy implements ClusterManager {

    private EventService eventService;

    public ClusterManagerEventServiceProxy(QMass qmass) {
        this.eventService = new TCPEventService(qmass);
    }

    @Override
    public void sendEvent(Event event) {
        eventService.sendEvent(event);
    }

    @Override
    public void sendEvent(InetSocketAddress to, Event event) {
        eventService.sendEvent(to, event);
    }

    @Override
    public InetSocketAddress getListening() {
        return eventService.getListening();
    }

    @Override
    public void start() {
        eventService.start();
    }

    @Override
    public void end() throws IOException {
        eventService.end();
    }

    @Override
    public void receiveEventAndDo(EventClosure closure) throws Exception {
        eventService.receiveEventAndDo(closure);
    }

    @Override
    public void addToCluster(InetSocketAddress listeningAt) {
        eventService.addToCluster(listeningAt);
    }

    @Override
    public void removeFromCluster(InetSocketAddress who) {
        eventService.removeFromCluster(who);
    }

    @Override
    public InetSocketAddress[] getCluster() {
        return eventService.getCluster();
    }

    @Override
    public Serializable getId() {
        return eventService.getId();
    }
}
