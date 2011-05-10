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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 17:47:45
 */
public class GreetEventHandler implements EventHandler {

    private static final Log logger = LogFactory.getLog(QMass.class);

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        GreetEvent ge = (GreetEvent) event;
        GreetService gs = (GreetService) service;
        getClusterManager(qmass).addToCluster(ge.getListeningAt());
        gs.greetIfHeDoesntKnowMe(ge.getListeningAt(), ge.getCluster());
        return this;
    }

    private DatagramClusterManager getClusterManager(QMass qmass) {
        return (DatagramClusterManager) qmass.getClusterManager();
    }

    private InetSocketAddress extractSocket(ByteBuffer buffer) {
        byte b = buffer.get();
        StringBuilder hostb = new StringBuilder();
        while (b != ',') {
            b = buffer.get();
            if (b != ',') {
                hostb.append((char) b);
            }
        }

        StringBuilder portb = new StringBuilder();
        while (b != ')') {
            b = buffer.get();
            if (b != ')') {
                portb.append((char) b);
            }
        }

        return new InetSocketAddress(hostb.toString(), Integer.valueOf(portb.toString()));
    }
}
