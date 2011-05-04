package org.mca.qmass.cache.hibernate.factory;

import org.hibernate.cache.*;
import org.hibernate.cache.access.SoftLock;
import org.mca.qmass.cache.QCache;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 10:54:17
 */
public class CollectionRegionAccessStrategy implements org.hibernate.cache.access.CollectionRegionAccessStrategy {

    private final CollectionRegion region;

    private final QCache qCache;

    public CollectionRegionAccessStrategy(CollectionRegion region, QCache qCache) {
        this.region = region;
        this.qCache = qCache;
    }

    @Override
    public org.hibernate.cache.CollectionRegion getRegion() {
        return region;
    }

    @Override
    public Object get(Object key, long txTimestamp) throws CacheException {
        return qCache.getSilently((Serializable) key);
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version) throws CacheException {
        qCache.put((Serializable)key,(Serializable)value);
        return true;
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) throws CacheException {
        qCache.put((Serializable)key,(Serializable)value);
        return true;
    }

    @Override
    public SoftLock lockItem(Object key, Object version) throws CacheException {
        return null;
    }

    @Override
    public SoftLock lockRegion() throws CacheException {
        return null;
    }

    @Override
    public void unlockItem(Object key, SoftLock lock) throws CacheException {
    }

    @Override
    public void unlockRegion(SoftLock lock) throws CacheException {
    }

    @Override
    public void remove(Object key) throws CacheException {
        qCache.remove((Serializable) key);
    }

    @Override
    public void removeAll() throws CacheException {
        qCache.clear();
    }

    @Override
    public void evict(Object key) throws CacheException {
        qCache.remove((Serializable) key);
    }

    @Override
    public void evictAll() throws CacheException {
        qCache.clear();
    }
}
