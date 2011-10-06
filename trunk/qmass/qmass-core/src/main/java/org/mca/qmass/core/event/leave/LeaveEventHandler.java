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
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 14:38:55
 */
public class LeaveEventHandler implements EventHandler {

    private static final YALog logger = YALogFactory.getLog(LeaveEventHandler.class);

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        LeaveEvent le = (LeaveEvent) event;
        LeaveService ls = (DefaultLeaveService) service;
        ls.removeFromCluster(new InetSocketAddress(le.getHostName(), le.getPort()));
        return this;
    }

}
