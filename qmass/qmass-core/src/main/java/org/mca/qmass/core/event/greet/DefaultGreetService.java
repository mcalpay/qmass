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
import org.mca.qmass.core.cluster.service.EventService;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 10:18:24
 */
public class DefaultGreetService implements GreetService {

    private static final YALog logger = YALogFactory.getLog(DefaultGreetService.class);

    private EventService eventService;

    private Scanner scanner;

    private InetSocketAddress listeningAt;

    private QMass qmass;

    private List<NodeGreetListener> listeners = new ArrayList<NodeGreetListener>();

    public DefaultGreetService(QMass qmass, EventService eventService, Scanner scanner) {
        this.listeningAt = eventService.getListening();
        this.scanner = scanner;
        this.eventService = eventService;
        this.qmass = qmass;
        qmass.registerService(this);
    }

    public DefaultGreetService(QMass qmass, EventService eventService) {
        this.listeningAt = eventService.getListening();
        this.eventService = eventService;
        this.qmass = qmass;
        qmass.registerService(this);
    }

    public Serializable getId() {
        return GreetService.class;
    }

    @Override
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public GreetService greet() {
        if (scanner != null) {
            InetSocketAddress to = scanner.scan();
            while (to != null) {
                logger.debug(listeningAt + " greets " + to);
                greet(to);
                to = scanner.scan();
            }
        } else {
            logger.debug(listeningAt + " greets multicast.");
            eventService.sendEvent(newEvent());
        }
        return this;
    }

    @Override
    public GreetService greet(InetSocketAddress add) {
        eventService.sendEvent(add, newEvent());
        return this;
    }

    private GreetEvent newEvent() {
        return new GreetEvent(qmass, this, listeningAt);
    }

    @Override
    public GreetService welcome(InetSocketAddress addressToAdd, InetSocketAddress[] cluster) {
        if (!listeningAt.equals(addressToAdd) &&
                !Arrays.asList(qmass.getEventService().getCluster()).contains(addressToAdd)) {
            logger.debug(listeningAt + " welcome " + addressToAdd);
            eventService.addToCluster(addressToAdd);
            if (!Arrays.asList(cluster).contains(listeningAt)) {
                greet(addressToAdd);
            }

            for (NodeGreetListener ngl : listeners) {
                ngl.greet(addressToAdd);
            }
        }
        return this;
    }


    @Override
    public GreetService registerNodeWelcomeListener(NodeGreetListener listener) {
        listeners.add(listener);
        InetSocketAddress[] cluster = qmass.getEventService().getCluster();
        for (InetSocketAddress who : cluster) {
            listener.greet(who);
        }
        return this;
    }
}
