package org.mca.qmass.core.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: malpay
 * Date: 26.Nis.2011
 * Time: 10:37:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractEvent implements Event {

    private ByteBuffer buffer;

    public AbstractEvent(QMass qm, Service service, Class handler) {
        StringBuilder builder = new StringBuilder(qm.getId().toString())
                .append("/").append(handler.getName())
                .append("/").append(service.getId())
                .append("/");
        append(builder.toString());
    }

    public AbstractEvent append(byte [] data) {
        if (buffer == null) {
            buffer = ByteBuffer.allocate(data.length);
        } else {
            byte [] contains = buffer.array();
            buffer = ByteBuffer.allocate(contains.length + data.length);
            buffer.put(contains);
        }
        buffer.put(data);
        return this;
    }

    public AbstractEvent append(String data) {
        append(data.getBytes());
        return this;
    }

    @Override
    public final ByteBuffer getBytes() {
        return buffer;
    }

}
