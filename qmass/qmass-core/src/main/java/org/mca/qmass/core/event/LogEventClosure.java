package org.mca.qmass.core.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 11:08:17
 */
public class LogEventClosure implements EventClosure {

    private static Log log = LogFactory.getLog(LogEventClosure.class);

    private static EventClosure instance = new LogEventClosure();

    private LogEventClosure() {
    }

    public static EventClosure getInstance() {

        return instance;
    }

    @Override
    public Object execute(Event event) throws Exception {
        log.debug("event : " + event);
        return this;
    }
}
