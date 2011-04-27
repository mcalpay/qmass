package org.mca.qmass.cache.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.cache.QCache;
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
    public EventHandler handleEvent(QMass qmass, ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            StringBuilder cacheId = new StringBuilder();
            StringBuilder cacheKey = new StringBuilder();
            byte b = buffer.get();
            while (b != '/') {
                cacheId.append((char) b);
                b = buffer.get();
            }

            while (buffer.hasRemaining()) {
                b = buffer.get();
                cacheKey.append((char) b);
            }

            QCache QCache = (QCache) qmass.getService(cacheId.toString());
            if (cacheKey.length() == 0) {
                QCache.clearSilently();
            } else {
                QCache.removeSilently(cacheKey.toString());
            }
            logger.debug("Removed " + cacheId + " " + cacheKey);
        }
        return this;
    }
}
