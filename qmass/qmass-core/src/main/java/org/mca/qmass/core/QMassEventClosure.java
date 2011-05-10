package org.mca.qmass.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.event.AbstractEvent;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.EventHandler;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 11:00:43
 */
public class QMassEventClosure implements EventClosure {

    private static final Log logger = LogFactory.getLog(QMassEventClosure.class);

    private QMass qmass;

    public QMassEventClosure(QMass qmass) {
        this.qmass = qmass;
    }

    @Override
    public Object execute(AbstractEvent event) throws Exception {
        Service service = qmass.getService(event.getServiceId());
        logger.debug(qmass.getClusterManager().getId() + ", " + qmass.getId() + " received; " + event + ", service : " + service);
        if (event.getId().equals(qmass.getId()) && service != null) {
            EventHandler handler = (EventHandler) Class.forName(event.getHandlerName()).newInstance();
            handler.handleEvent(qmass, service, event);
        }
        return this;
    }
}
