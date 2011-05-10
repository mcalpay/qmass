package org.mca.qmass.core.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 11:08:17
 */
public class NOOPEventClosure implements EventClosure {

    private static Log log = LogFactory.getLog(NOOPEventClosure.class);

    private static EventClosure instance = new NOOPEventClosure();

    private NOOPEventClosure() {
    }

    public static EventClosure getInstance() {

        return instance;
    }

    @Override
    public Object execute(QMassEvent event) throws Exception {
        log.debug("event : " + event);
        return this;
    }
}
