/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mca.qmass.cache.hibernate.factory;

import org.hibernate.cache.*;
import org.hibernate.cache.access.SoftLock;
import org.mca.qmass.cache.QCache;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 11:05:59
 */
public class EntityRegionAccessStrategy implements org.hibernate.cache.access.EntityRegionAccessStrategy {

    private final EntityRegion region;

    private final QCache qCache;

    public EntityRegionAccessStrategy(EntityRegion region, QCache qCache) {
        this.region = region;
        this.qCache = qCache;
    }

    @Override
    public org.hibernate.cache.EntityRegion getRegion() {
        return region;
    }

    @Override
    public Object get(Object key, long txTimestamp)  throws CacheException {
        return qCache.getSilently((Serializable) key);
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version) throws CacheException {
        qCache.put((Serializable) key, (Serializable) value);
        return true;
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) throws CacheException {
        qCache.put((Serializable) key, (Serializable) value);
        return true;
    }

    @Override
    public SoftLock lockItem(Object key, Object version)  throws CacheException {
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
    public boolean insert(Object key, Object value, Object version)  throws CacheException {
        qCache.put((Serializable) key, (Serializable) value);
        return true;
    }

    @Override
    public boolean afterInsert(Object key, Object value, Object version)  throws CacheException {
        qCache.put((Serializable) key, (Serializable) value);
        return true;
    }

    @Override
    public boolean update(Object key, Object value, Object currentVersion, Object previousVersion) throws CacheException {
        qCache.put((Serializable) key, (Serializable) value);
        return true;
    }

    @Override
    public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock)  throws CacheException {
        qCache.put((Serializable) key, (Serializable) value);
        return true;
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
