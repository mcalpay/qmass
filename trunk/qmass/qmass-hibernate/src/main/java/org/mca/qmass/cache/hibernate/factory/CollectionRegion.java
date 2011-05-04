package org.mca.qmass.cache.hibernate.factory;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.access.AccessType;
import org.mca.qmass.cache.DefaultQCache;
import org.mca.qmass.core.QMass;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 10:47:16
 */
public class CollectionRegion extends TransactionalRegion implements org.hibernate.cache.CollectionRegion {

    public CollectionRegion(String regionName, CacheDataDescription metadata, QMass qmass) {
        super(regionName, metadata, new DefaultQCache(regionName, qmass, null, null));
    }

    @Override
    public org.hibernate.cache.access.CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return new CollectionRegionAccessStrategy(this,qCache);
    }
}
