package org.mca.qmass.core.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.QMassEventClosure;
import org.mca.qmass.core.event.EventClosure;

/**
 * User: malpay
 * Date: 01.Aðu.2011
 * Time: 14:14:19
 */
public class RunnableEventManager implements Runnable {

    private static final Log logger = LogFactory.getLog(RunnableEventManager.class);

    private EventManager eventManager;

    private QMass qmass;

    private EventClosure eventClosure;

    private boolean running = true;

    public RunnableEventManager(EventManager eventManager, QMass qmass) {
        this.eventManager = eventManager;
        this.eventClosure = new QMassEventClosure(qmass);

    }

    @Override
    public void run() {
        while (running) {
            try {
                eventManager.receiveEventAndDo(eventClosure);
            } catch (Exception e) {
                logger.error("had error trying to handle event", e);
            }
        }
    }

    public void end() {
        running = false;
    }
}
