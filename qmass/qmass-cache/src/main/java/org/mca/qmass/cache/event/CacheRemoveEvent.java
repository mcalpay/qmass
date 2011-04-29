package org.mca.qmass.cache.event;

import org.mca.qmass.cache.QCache;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.AbstractEvent;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 11:28:26
 */
public class CacheRemoveEvent extends AbstractEvent {

    public CacheRemoveEvent(QMass qm, Service service, Serializable cacheKey) {
        super(qm, service, CacheRemoveEventHandler.class);
        append(cacheKey.toString());
    }

    public CacheRemoveEvent(QMass qm, Service service) {
        super(qm, service, CacheRemoveEventHandler.class);
    }

}
