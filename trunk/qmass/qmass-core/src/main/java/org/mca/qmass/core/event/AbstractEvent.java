package org.mca.qmass.core.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: malpay
 * Date: 26.Nis.2011
 * Time: 10:37:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractEvent implements Event {

    private StringBuilder builder;

    public AbstractEvent(QMass qm, Service service, Class handler) {
        builder = new StringBuilder(qm.getId().toString())
                .append("/").append(handler.getName())
                .append("/").append(service.getId())
                .append("/");
    }

    protected StringBuilder append(Object str) {
        builder.append(str);
        return builder;
    }

    @Override
    public final byte[] getBytes() {
        return builder.toString().getBytes();
    }

}
