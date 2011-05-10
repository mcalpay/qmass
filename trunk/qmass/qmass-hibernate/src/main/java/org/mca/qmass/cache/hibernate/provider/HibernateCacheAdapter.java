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
package org.mca.qmass.cache.hibernate.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.Timestamper;
import org.mca.qmass.cache.DefaultQCache;
import org.mca.qmass.cache.QCache;
import org.mca.qmass.cache.ReplicatedQCache;
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

    protected final Log logger = LogFactory.getLog(getClass());

    private QCache qCache;

    public HibernateCacheAdapter(String region, QMass qmass) {
        if ("org.hibernate.cache.UpdateTimestampsCache".equals(region)
                || "org.hibernate.cache.StandardQueryCache".equals(region)) {
            qCache = new ReplicatedQCache(region, qmass, null, new ArrayList(),
                    false,
                    false);
        } else {
            qCache = new ReplicatedQCache(region, qmass, null, new ArrayList(),
                    qmass.getIR().getReplicateUpdates(),
                    qmass.getIR().getReplicateInserts());
        }
    }

    @Override
    public Object read(Object key) throws CacheException {
        return get(key);
    }

    @Override
    public Object get(Object key) throws CacheException {
        return qCache.getSilently((Serializable) key);
    }

    @Override
    public void put(Object key, Object val) throws CacheException {
        qCache.put((Serializable) key, (Serializable) val);
    }

    @Override
    public void update(Object key, Object val) throws CacheException {
        qCache.put((Serializable) key, (Serializable) val);
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
