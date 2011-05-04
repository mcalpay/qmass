package org.mca.qmass.cache.hibernate.factory;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.Timestamper;
import org.mca.qmass.cache.DefaultQCache;
import org.mca.qmass.cache.QCache;
import org.mca.qmass.core.QMass;

import java.util.Map;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 10:32:31
 */
public class Region implements org.hibernate.cache.Region {

    protected final QCache qCache;

    private final String regionName;

    public Region(String regionName,QMass qmass) {
        this.regionName = regionName;
        this.qCache = new DefaultQCache(regionName, qmass, null, null);
    }

    public Region(String regionName, QCache qCache) {
        this.regionName = regionName;
        this.qCache = qCache;
    }

    @Override
    public String getName() {
        return regionName;
    }

    @Override
    public void destroy() throws CacheException {
        qCache.clear();
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

    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }

    @Override
    public int getTimeout() {
        return 0;
    }
}
