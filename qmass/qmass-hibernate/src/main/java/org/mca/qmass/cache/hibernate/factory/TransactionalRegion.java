package org.mca.qmass.cache.hibernate.factory;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.TransactionalDataRegion;
import org.mca.qmass.cache.QCache;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 10:38:21
 */
public class TransactionalRegion extends Region implements TransactionalDataRegion {

    protected final CacheDataDescription metadata;

    public TransactionalRegion(String regionName, CacheDataDescription metadata, QCache qCache) {
        super(regionName, qCache);
        this.metadata = metadata;
    }

    @Override
    public boolean isTransactionAware() {
        return false;
    }

    @Override
    public CacheDataDescription getCacheDataDescription() {
        return metadata;
    }
}
