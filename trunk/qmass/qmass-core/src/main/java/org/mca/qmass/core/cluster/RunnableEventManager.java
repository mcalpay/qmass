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
import org.mca.qmass.core.QMassEventClosure;
import org.mca.qmass.core.event.EventClosure;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.Serializable;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: malpay
 * Date: 01.Agu.2011
 * Time: 14:14:19
 */
public class RunnableEventManager implements Runnable {

    private static final YALog logger = YALogFactory.getLog(RunnableEventManager.class);

    private List<EventManager> eventManagerList;

    private EventClosure eventClosure;

    private boolean running = true;
    // TODO no need to pool...
    private final ExecutorService eventExecutor = Executors.newFixedThreadPool(1);

    public RunnableEventManager(QMass qmass) {
        this.eventManagerList = new ArrayList<EventManager>();
        this.eventClosure = new QMassEventClosure(qmass);
    }

    public RunnableEventManager add(EventManager em) {
        this.eventManagerList.add(em);
        logger.info(em + " added to list.");
        return this;
    }

    @Override
    public void run() {
        while (running) {
            try {
                for (EventManager eventManager : eventManagerList) {
                    eventManager.receiveEventAndDo(eventClosure);
                }
            } catch (ClosedChannelException e) {
                logger.debug("closed channel");
            } catch (Exception e) {
                logger.error("had error trying to handle event", e);
            }
        }
    }

    public RunnableEventManager execute() {
        eventExecutor.execute(this);
        logger.info("pool executed.");
        return this;
    }

    public void end() {
        running = false;
        eventExecutor.shutdown();
        logger.info("ended.");
    }
}
