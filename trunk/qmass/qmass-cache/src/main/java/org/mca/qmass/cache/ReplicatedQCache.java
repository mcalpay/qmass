package org.mca.qmass.cache;

import org.mca.qmass.cache.event.CachePutEvent;
import org.mca.qmass.cache.event.CacheRemoveEvent;
import org.mca.qmass.core.QMass;

import java.io.Serializable;
import java.util.List;

/**
 * User: malpay
 * Date: 29.Nis.2011
 * Time: 14:57:07
 */
public class ReplicatedQCache extends DefaultQCache {

    public ReplicatedQCache(Serializable id, QMass qmass, QCache parent, List<QCache> children) {
        super(id, qmass, parent, children);
    }

    @Override
    protected QCache doOnUpdate(Serializable key, Object value) {
        qmass.sendEvent(new CachePutEvent(qmass, this, key, value));
        return this; 
    }
}
