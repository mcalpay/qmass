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
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.greet.GreetEvent;
import org.mca.qmass.core.scanner.Scanner;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 10:18:24
 */
public class DefaultGreetService implements GreetService {

    private Serializable id;

    private QMass qmass;

    private InetSocketAddress listeningAt;

    private Scanner scanner;

    public Serializable getId() {
        return id;
    }

    public DefaultGreetService(QMass qmass, InetSocketAddress listeningAt, Scanner scanner) {
        this.id = qmass.getId() + "greet";
        this.qmass = qmass;
        this.listeningAt = listeningAt;
        this.scanner = scanner;
        this.qmass.registerService(this);
    }

    @Override
    public GreetService greet() {
        sendEvent(scanner,
                new GreetEvent(qmass, this, listeningAt));
        return this;
    }

    private GreetService sendEvent(Scanner scanner, Event event) {
        InetSocketAddress to = scanner.scan();
        while (to != null) {
            getClusterManager().safeSendEvent(to,event);
            to = scanner.scan();
        }
        return this;
    }
    
    private DatagramClusterManager getClusterManager() {
        return (DatagramClusterManager) qmass.getClusterManager();
    }

    @Override
    public GreetService greetIfHeDoesntKnowMe(InetSocketAddress who, InetSocketAddress[] knowsWho) {
        if (!Arrays.asList(knowsWho).contains(listeningAt)) {
            getClusterManager().safeSendEvent(who, new GreetEvent(qmass, this, listeningAt));
        }
        return this;
    }
}
