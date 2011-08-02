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
import org.mca.qmass.core.cluster.service.EventService;
import org.mca.qmass.core.scanner.Scanner;

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

    protected final Log logger = LogFactory.getLog(getClass());

    private Serializable id;

    private EventService eventService;

    private Scanner scanner;

    private InetSocketAddress listeningAt;

    private QMass qmass;

    private List<NodeGreetListener> listeners = new ArrayList<NodeGreetListener>();

    public DefaultGreetService(QMass qmass, EventService eventService, Scanner scanner) {
        this.id = qmass.getId() + "/Greet";
        this.listeningAt = eventService.getListening();
        this.scanner = scanner;
        this.eventService = eventService;
        this.qmass = qmass;
        qmass.registerService(this);
    }

    public Serializable getId() {
        return id;
    }

    @Override
    public GreetService greet() {
        InetSocketAddress to = scanner.scan();
        while (to != null) {
            greet(to);
            to = scanner.scan();
        }
        return this;
    }

    @Override
    public GreetService greet(InetSocketAddress add) {
        eventService.sendEvent(add, new GreetEvent(qmass, this, listeningAt));
        return this;
    }

    @Override
    public GreetService welcome(InetSocketAddress addressToAdd, InetSocketAddress[] cluster) {
        eventService.addToCluster(addressToAdd);
        if (!Arrays.asList(cluster).contains(listeningAt)) {
            greet(addressToAdd);
        }

        for (NodeGreetListener ngl : listeners) {
            ngl.greet(addressToAdd);
        }
        return this;
    }


    @Override
    public GreetService registerNodeWelcomeListener(NodeGreetListener listener) {
        listeners.add(listener);
        InetSocketAddress[] cluster = qmass.getClusterManager().getCluster();
        for (InetSocketAddress who : cluster) {
            listener.greet(who);
        }
        return this;
    }
}
