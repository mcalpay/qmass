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

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: malpay
 * Date: 17.10.2011
 * Time: 10:51
 * <p/>
 * Queue and Thread
 */
public class QueuedPersistenceService implements PersistenceService {

    private AbstractQueueRunnable persisterQueue;

    private AbstractQueueRunnable removerQueue;

    private Thread persisterThread;

    private Thread removerThread;

    private TupleStore tupleStore;

    private String type;

    public QueuedPersistenceService(String type) {
        this.persisterQueue = new PersisterQueueRunnable();
        this.removerQueue = new RemoverQueueRunnable();
        persisterThread = new Thread(persisterQueue, PersisterQueueRunnable.class.getSimpleName());
        removerThread = new Thread(removerQueue, RemoverQueueRunnable.class.getSimpleName());
        this.type = type;
    }

    @Override
    public Serializable get(Serializable key) {
        if (tupleStore == null) {
            tupleStore = new MongoDBTupleStore();
        }
        return tupleStore.get(new Tuple(type, key));
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
    public void end() {
        persisterThread.interrupt();
        removerThread.interrupt();
    }

}
