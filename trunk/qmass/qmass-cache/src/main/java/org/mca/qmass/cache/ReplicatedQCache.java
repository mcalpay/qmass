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

    private boolean replicateUpdates = true;

    private boolean replicateInserts = true;

    public ReplicatedQCache(Serializable id, QMass qmass, QCache parent, List<QCache> children) {
        super(id, qmass, parent, children);
    }

    public ReplicatedQCache(Serializable id, QMass qmass, QCache parent, List<QCache> children,
                            boolean replicateUpdates, boolean replicateInserts) {
        super(id, qmass, parent, children);
        this.replicateUpdates = replicateUpdates;
        this.replicateInserts = replicateInserts;
        logger.debug("building cache :" + id + ", replicate updates : " + replicateUpdates + ", replicate inserts : " + replicateInserts);        
    }

    @Override
    protected QCache doOnUpdate(Serializable key, Serializable value) {
        if (replicateUpdates) {
            qmass.sendEvent(new CachePutEvent(qmass, this, key, value));
        }
        return this;
    }

    @Override
    protected QCache doOnInsert(Serializable key, Serializable value) {
        if (replicateInserts) {
            qmass.sendEvent(new CachePutEvent(qmass, this, key, value));
        }
        return this;
    }
}
