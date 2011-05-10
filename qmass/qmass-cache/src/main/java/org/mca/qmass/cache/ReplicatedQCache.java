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

    private boolean replicateUpdates;

    private boolean replicateInserts;

    public ReplicatedQCache(Serializable id, QMass qmass, QCache parent, List<QCache> children) {
        super(id, qmass, parent, children);
        this.replicateUpdates = qmass.getIR().getReplicateUpdates();
        this.replicateInserts = qmass.getIR().getReplicateInserts();
        logger.debug("building cache :" + id + ", replicate updates : " + replicateUpdates + ", replicate inserts : " + replicateInserts);
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

    @Override
    public String toString() {
        return "ReplicatedQCache{" +
                "replicateUpdates=" + replicateUpdates +
                ", replicateInserts=" + replicateInserts +
                "} " + super.toString();
    }
}
