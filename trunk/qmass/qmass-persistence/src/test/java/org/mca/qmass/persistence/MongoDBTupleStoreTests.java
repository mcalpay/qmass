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


import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * User: malpay
 * Date: 17.10.2011
 * Time: 11:35
 */
public class MongoDBTupleStoreTests {

    private MongoDBTupleStore db;

    public MongoDBTupleStoreTests() {
        this.db = new MongoDBTupleStore();
    }

    @Test
    public void persistGetRemoveWorks() {
        Tuple test = new Tuple("testType","testKey","test");
        db.persist(test);
        Tuple p = db.get(test);
        assertEquals(test, p);
        db.remove(test);
        assertNull(db.get(test));
    }

}
