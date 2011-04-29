package org.mca.qmass.cache.event;

import org.mca.qmass.cache.ReplicatedQCache;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.AbstractEvent;
import org.mca.qmass.core.event.Event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 29.Nis.2011
 * Time: 15:00:08
 */
public class CachePutEvent extends AbstractEvent {

    public CachePutEvent(QMass qm, Service service,
                         Serializable key, Object value) {
        super(qm, service, CachePutEventHandler.class);
        append(key.toString()).append("/");
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new ObjectOutputStream(bos).writeObject(value);
            byte[] bytes1 = bos.toByteArray();
            append(bytes1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
