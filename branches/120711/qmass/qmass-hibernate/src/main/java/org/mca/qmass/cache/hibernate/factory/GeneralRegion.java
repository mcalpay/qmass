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

import org.hibernate.cache.CacheException;
import org.hibernate.cache.QueryResultsRegion;
import org.hibernate.cache.Timestamper;
import org.hibernate.cache.TimestampsRegion;
import org.mca.qmass.cache.DefaultQCache;
import org.mca.qmass.cache.QCache;
import org.mca.qmass.core.QMass;

import java.io.Serializable;
import java.util.Map;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 10:11:48
 */
public class GeneralRegion extends Region implements TimestampsRegion, QueryResultsRegion {

    public GeneralRegion(String regionName, QMass qmass) {
        super(regionName,new DefaultQCache(regionName, qmass, null, null));
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
    public void evict(Object key) throws CacheException {
        qCache.remove((Serializable) key);
    }

    @Override
    public void evictAll() throws CacheException {
        qCache.clear();
    }

}
