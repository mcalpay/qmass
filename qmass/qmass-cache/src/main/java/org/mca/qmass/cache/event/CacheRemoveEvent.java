package org.mca.qmass.cache.event;

import org.mca.qmass.core.event.AbstractEvent;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 11:28:26
 */
public class CacheRemoveEvent extends AbstractEvent {

    public CacheRemoveEvent(Serializable id, Serializable cacheId, Serializable cacheKey) {
        super(id, CacheRemoveEventHandler.class);
        append(cacheId).append("/").append(cacheKey);
    }

    public CacheRemoveEvent(Serializable id, Serializable cacheId) {
        super(id, CacheRemoveEventHandler.class);
        append(cacheId).append("/");
    }

}
