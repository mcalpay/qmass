package org.mca.qmass.cache.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.cache.QCache;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.EventHandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

/**
 * User: malpay
 * Date: 29.Nis.2011
 * Time: 15:07:06
 */
public class CachePutEventHandler implements EventHandler {

    private final static Log logger = LogFactory.getLog(CacheRemoveEventHandler.class);

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, ByteBuffer buffer) {
        StringBuilder cacheKey = new StringBuilder();
        byte b = buffer.get();
        while (b != '/') {
            cacheKey.append((char) b);
            b = buffer.get();
        }
        try {
            int len = buffer.limit() - buffer.position();
            byte[] buf = new byte[buffer.remaining()];
            buffer.get(buf);
            Object value = new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
            QCache qcache = (QCache) service;
            logger.debug("put received " + cacheKey.toString() + " , " + value);
            qcache.putSilently(cacheKey.toString(), value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        return this;
    }
}
