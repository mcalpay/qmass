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

    private MongoDBTupleStore tupleStore;

    public MongoDBTupleStoreTests() {
        this.tupleStore = new MongoDBTupleStore();
    }

    @Test
    public void persistGetUpdateRemoveWorks() throws Exception {
        Tuple test = new Tuple("testType", "testKey", "test");
        tupleStore.persist(test);
        Tuple p = tupleStore.get(test);
        assertEquals(test, p);
        assertEquals(test.getValue(), p.getValue());

        test = new Tuple("testType", "testKey", "test1");
        tupleStore.persist(test);
        p = tupleStore.get(test);
        assertEquals(test.getValue(), p.getValue());

        tupleStore.remove(test);
        assertNull(tupleStore.get(test));
    }

    @Test
    public void cursor() {
        tupleStore.persist(new Tuple("testType", "key1", "val1"));
        tupleStore.persist(new Tuple("testType", "key2", "val2"));

        Cursor cursor = tupleStore.getCursor(new TrueFilterPredicate("testType"));
        assertEquals("val1", cursor.next());
        assertEquals("val2", cursor.next());
        assertNull(cursor.next());

    }

}
