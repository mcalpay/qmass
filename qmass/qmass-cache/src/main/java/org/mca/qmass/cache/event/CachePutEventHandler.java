package org.mca.qmass.cache.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.cache.QCache;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 29.Nis.2011
 * Time: 15:07:06
 */
public class CachePutEventHandler implements EventHandler {

    private final static Log logger = LogFactory.getLog(CacheRemoveEventHandler.class);

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        CachePutEvent cpe = (CachePutEvent) event;
        Serializable cacheKey = cpe.getKey();
        Serializable value = cpe.getValue();
        QCache qcache = (QCache) service;
        qcache.putSilently(cacheKey, value);
        return this;
    }
}
