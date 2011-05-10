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

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.access.AccessType;
import org.mca.qmass.cache.ReplicatedQCache;
import org.mca.qmass.core.QMass;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 10:47:16
 */
public class CollectionRegion extends TransactionalRegion implements org.hibernate.cache.CollectionRegion {

    public CollectionRegion(String regionName, CacheDataDescription metadata, QMass qmass) {
        super(regionName, metadata, new ReplicatedQCache(regionName, qmass, null, null));
    }

    @Override
    public org.hibernate.cache.access.CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return new CollectionRegionAccessStrategy(this,qCache);
    }
}
