package org.mca.qmass.event;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;

/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 10:36:23
 */
public class LogChangeEventHandler implements EventHandler {
    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        LogChangeEvent lce = (LogChangeEvent) event;
        setLoggerLevel(lce.getClazz(), lce.getLevel());
        return this;
    }

    void setLoggerLevel(String clazz, String level) {
        Logger logger = LogManager.getLogger(clazz);
        if ("DEBUG".equals(level)) {
            logger.setLevel(Level.DEBUG);
        } else if ("WARN".equals(level)) {
            logger.setLevel(Level.WARN);
        } else if ("INFO".equals(level)) {
            logger.setLevel(Level.INFO);
        } else if ("TRACE".equals(level)) {
            logger.setLevel(Level.TRACE);
        } else if ("ERROR".equals(level)) {
            logger.setLevel(Level.ERROR);
        }
    }
}
