package org.mca.qmass.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.NOOPService;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 11.May.2011
 * Time: 17:58:42
 */
public class DefaultLogService implements LogService {

    private Serializable id;

    private QMass mass;

    public Serializable getId() {
        return id;
    }

    public DefaultLogService(Serializable id, QMass mass) {
        this.id = id;
        this.mass = mass;
        mass.registerService(this);
    }

    @Override
    public LogService changeLog(String clazz, String level) {
        mass.sendEvent(new LogChangeEvent(mass, this, clazz, level));
        return this;
    }
}
