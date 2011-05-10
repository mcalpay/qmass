package org.mca.qmass.core.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 17:37:40
 */
public class Event implements Serializable {

    protected final Log logger = LogFactory.getLog(getClass());

    private Serializable id;

    private String handlerName;

    private Serializable serviceId;

    public Event(QMass qm, Service service, Class handler) {
        this.id = qm.getId();
        this.serviceId = service.getId();
        this.handlerName = handler.getName();
    }

    public Event() {
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(id);
        out.writeObject(handlerName);
        out.writeObject(serviceId);
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        id = (Serializable) in.readObject();
        handlerName = (String) in.readObject();
        serviceId = (Serializable) in.readObject();
    }

    public Serializable getId() {
        return id;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public Serializable getServiceId() {
        return serviceId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", handler=" + ((handlerName == null) ? null :
                handlerName.substring(handlerName.lastIndexOf('.') + 1)) +
                ", service=" + serviceId +
                '}';
    }
}
