package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.QMassEventClosure;
import org.mca.qmass.core.event.EventClosure;

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

    private static final Log logger = LogFactory.getLog(RunnableEventManager.class);

    private List<EventManager> eventManagerList;

    private EventClosure eventClosure;

    private boolean running = true;

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
                //Thread.yield();
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
