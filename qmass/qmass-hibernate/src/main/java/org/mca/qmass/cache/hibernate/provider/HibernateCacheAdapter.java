package org.mca.qmass.cache.hibernate.provider;

import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.Timestamper;
import org.mca.qmass.cache.DefaultQCache;
import org.mca.qmass.cache.QCache;
import org.mca.qmass.core.QMass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 14:55:56
 */
public class HibernateCacheAdapter implements Cache {

    private QCache qCache;

    public HibernateCacheAdapter(String region, QMass qmass) {
        qCache = new DefaultQCache(region, qmass, null, new ArrayList());
    }

    @Override
    public Object read(Object key) throws CacheException {
        return qCache.get((Serializable) key);
    }

    @Override
    public Object get(Object key) throws CacheException {
        return qCache.get((Serializable) key);
    }

    @Override
    public void put(Object key, Object val) throws CacheException {
        qCache.put((Serializable) key, val);
    }

    @Override
    public void update(Object key, Object val) throws CacheException {
        qCache.put((Serializable) key, val);
    }

    @Override
    public void remove(Object key) throws CacheException {
        qCache.remove((Serializable) key);
    }

    @Override
    public void clear() throws CacheException {
        qCache.clear();
    }

    @Override
    public void destroy() throws CacheException {
        qCache.clear();
    }

    @Override
    public void lock(Object o) throws CacheException {
    }

    @Override
    public void unlock(Object o) throws CacheException {
    }

    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }

    @Override
    public int getTimeout() {
        return 0;  
    }

    @Override
    public String getRegionName() {
        return (String) qCache.getId();
    }

    @Override
    public long getSizeInMemory() {
        return 0;
    }

    @Override
    public long getElementCountInMemory() {
        return 0;
    }

    @Override
    public long getElementCountOnDisk() {
        return 0;
    }

    @Override
    public Map toMap() {
        return null;
    }
}
