package org.mca.qmass.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.QMassEvent;

/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 10:35:52
 */
public class LogChangeEvent extends QMassEvent {
    private String clazz;

    private String level;

    public LogChangeEvent(QMass qm, Service service, String clazz, String level) {
        super(qm, service, LogChangeEventHandler.class);
        this.clazz = clazz;
        this.level = level;
    }

    public String getClazz() {
        return clazz;
    }

    public String getLevel() {
        return level;
    }
}
