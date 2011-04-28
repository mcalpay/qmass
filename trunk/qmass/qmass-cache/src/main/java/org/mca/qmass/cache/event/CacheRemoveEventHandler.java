package org.mca.qmass.cache.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.cache.QCache;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.EventHandler;
import org.mca.qmass.core.QMass;

import java.nio.ByteBuffer;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 12:10:05
 */
public class CacheRemoveEventHandler implements EventHandler {

    private final static Log logger = LogFactory.getLog(CacheRemoveEventHandler.class);

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, ByteBuffer buffer) {
        StringBuilder cacheKey = new StringBuilder();
        if (buffer.hasRemaining()) {
            byte b;
            while (buffer.hasRemaining()) {
                b = buffer.get();
                cacheKey.append((char) b);
            }
        }
        
        QCache qcache = (QCache) service;
        if (cacheKey.length() == 0) {
            qcache.clearSilently();
        } else {
            qcache.removeSilently(cacheKey.toString());
        }
        logger.debug("Removed : " + qcache + " " + cacheKey);
        return this;
    }
}
