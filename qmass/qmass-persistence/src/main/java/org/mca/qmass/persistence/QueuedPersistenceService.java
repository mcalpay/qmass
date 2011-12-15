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
package org.mca.qmass.persistence;

import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.Serializable;
import java.util.List;

/**
 * User: malpay
 * Date: 17.10.2011
 * Time: 10:51
 * <p/>
 * Queue and Thread
 */
public class QueuedPersistenceService implements PersistenceService {

    private static final YALog log = YALogFactory.getLog(QueuedPersistenceService.class);

    private AbstractQueueThreadTemplate persisterQueue;

    private AbstractQueueThreadTemplate removerQueue;

    private TupleStore tupleStore;

    private String type;

    public QueuedPersistenceService(String type, TupleStore tupleStore) {
        this.tupleStore = tupleStore;
        this.persisterQueue = new PersisterQueue(tupleStore);
        this.removerQueue = new RemoverQueue(tupleStore);
        this.persisterQueue.start();
        this.removerQueue.start();
        this.type = type;
    }

    @Override
    public Serializable get(Serializable key) {
        Tuple tuple = tupleStore.get(new Tuple(type, key));
        if (tuple != null) {
            return tuple.getValue();
        }

        return null;
    }

    @Override
    public void persist(Serializable key, Serializable obj) {
        persisterQueue.queue(new Tuple(type, key, obj));
    }

    @Override
    public void remove(Serializable key) {
        removerQueue.queue(new Tuple(type, key));
    }

    @Override
    public List find(FilterPredicate predicate) {
        return tupleStore.find(type, predicate);
    }

    @Override
    public void end() {
        persisterQueue.interrupt();
        removerQueue.interrupt();
    }

}
