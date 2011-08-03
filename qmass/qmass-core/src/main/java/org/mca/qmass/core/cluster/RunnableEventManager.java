package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.QMassEventClosure;
import org.mca.qmass.core.event.EventClosure;

import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: malpay
 * Date: 01.Aðu.2011
 * Time: 14:14:19
 */
public class RunnableEventManager implements Runnable {

    private static final Log logger = LogFactory.getLog(RunnableEventManager.class);

    private EventManager eventManager;

    private EventClosure eventClosure;

    private boolean running = true;

    private final ExecutorService eventExecutor = Executors.newFixedThreadPool(1);

    public RunnableEventManager(EventManager eventManager, QMass qmass) {
        this.eventManager = eventManager;
        this.eventClosure = new QMassEventClosure(qmass);
    }

    @Override
    public void run() {
        while (running) {
            try {
                eventManager.receiveEventAndDo(eventClosure);
                Thread.yield();
            } catch (ClosedChannelException e) {
                logger.debug("closed channel");
            } catch (Exception e) {
                logger.error("had error trying to handle event", e);
            }
        }
    }

    public RunnableEventManager execute() {
        eventExecutor.execute(this);
        return this;
    }

    public void end() {
        running = false;
        eventExecutor.shutdown();
    }
}
