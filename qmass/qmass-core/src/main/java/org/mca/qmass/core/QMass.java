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
package org.mca.qmass.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.ir.IR;
import org.mca.qmass.core.cluster.ClusterManager;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.cluster.MulticastClusterManager;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.NOOPService;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.ir.QMassIR;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 14:13:09
 */
public class QMass {

    private static final Log logger = LogFactory.getLog(QMass.class);

    private static final Map<Serializable, QMass> masses = new HashMap<Serializable, QMass>();

    private Serializable id;

    private Timer timer;

    private Map<Serializable, Service> services = new HashMap();

    private ClusterManager clusterManager;

    private static final QMassIR DEFAULT_IR = IR.<QMassIR>get(QMassIR.DEFAULT, QMassIR.QMASS_IR);

    private EventClosure handleEvent;

    private boolean running = true;

    public static QMass getQMass() {
        QMass mass = masses.get(DEFAULT_IR.DEFAULT);
        if (mass == null) {
            mass = new QMass(DEFAULT_IR.DEFAULT);
            masses.put(DEFAULT_IR.DEFAULT, mass);
        }
        return mass;
    }

    public static QMass getQMass(Serializable id) {
        QMass mass = masses.get(id);
        if (mass == null) {
            mass = new QMass(id);
            masses.put(id, mass);
        }
        return mass;
    }

    public ClusterManager getClusterManager() {
        return clusterManager;
    }

    public QMassIR getIR() {
        QMassIR massIR = IR.get(id, QMassIR.QMASS_IR);
        if (massIR == null) {
            return new DefaultQMassIR();
        }
        return massIR;
    }

    public QMass(Serializable id) {
        logger.info("QMass is starting, id : " + id);
        IR.putIfDoesNotContain(id, QMassIR.QMASS_IR, DEFAULT_IR);
        this.id = id;
        this.handleEvent = new QMassEventClosure(this);
        if (getIR().getMulticastAddress().isEmpty()) {
            this.clusterManager = new DatagramClusterManager(this);
        } else {
            this.clusterManager = new MulticastClusterManager(this.getIR());
        }
        this.clusterManager.start();
        this.timer = new Timer();
        this.timer.start();
        registerService(NOOPService.getInstance());
    }

    public Serializable getId() {
        return id;
    }

    public QMass sendEvent(Event event) {
        try {
            this.clusterManager.sendEvent(event);
        } catch (IOException e) {
            logger.error(clusterManager.getId() + " had error trying to send event", e);
        }
        return this;
    }

    private QMass handleEvent() {
        try {
            this.clusterManager.receiveEventAndDo(handleEvent);
        } catch (Exception e) {
            logger.error(clusterManager.getId() + " had error trying to handle event", e);
        }
        return this;
    }

    public QMass end() {
        masses.remove(id);
        this.running = false;
        try {
            this.clusterManager.end();
        } catch (IOException e) {
            logger.error(clusterManager.getId() + " had error ending", e);
        }
        return this;
    }

    public Service getService(Serializable id) {
        return services.get(id);
    }

    public QMass registerService(Service service) {
        services.put(service.getId(), service);
        return this;
    }

    public QMass unRegisterService(Service service) {
        services.remove(service.getId());
        return this;
    }

    private class Timer extends Thread {

        @Override
        public void run() {
            while (running) {
                try {
                    handleEvent();
                    Thread.sleep(getIR().getDefaultThreadWait());
                } catch (InterruptedException e) {
                    logger.warn("interrupted.", e);
                }
            }
        }

        private void end() {
            running = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QMass mass = (QMass) o;

        if (!id.equals(mass.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "QMass{id=" + id +
                ", " + clusterManager +
                '}';
    }
}
