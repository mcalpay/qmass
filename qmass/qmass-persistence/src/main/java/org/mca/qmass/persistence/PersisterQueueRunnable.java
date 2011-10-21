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

/**
 * User: malpay
 * Date: 20.10.2011
 * Time: 15:54
 */
public class PersisterQueueRunnable extends AbstractQueueRunnable<Tuple> {

    private TupleStore tupleStore;

    @Override
    protected void initJustBeforeRun() {
        tupleStore = new MongoDBTupleStore();
    }

    @Override
    protected void doOnQueuedElement(Tuple take) {
        tupleStore.persist(take);
    }

}
