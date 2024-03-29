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

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 14.11.2011
 * Time: 09:25
 */
public class MongoDBColCursor implements Cursor<Serializable> {

    private int currIndex = 0;

    private DBCursor dbCursor;

    private TupleStore tupleStore;

    private FilterPredicate predicate;

    private String type;

    public MongoDBColCursor(DBCursor dbCursor, TupleStore tupleStore,
                            FilterPredicate predicate,
                            String type) {
        this.dbCursor = dbCursor;
        this.tupleStore = tupleStore;
        this.predicate = predicate;
        this.type = type;
    }

    @Override
    public boolean hasNext() {
        return dbCursor.hasNext();
    }

    @Override
    public Serializable next() {
        while (dbCursor.hasNext()) {
            DBObject next = dbCursor.next();
            final Serializable value = tupleStore.get(
                    new Tuple(type, (Serializable) next.get("key"))).getValue();
            if (predicate.filterInToResults(value)) {
                currIndex++;
                return value;
            }
        }
        return null;
    }

    @Override
    public void remove() {
        dbCursor.remove();
    }

    @Override
    public int currIndex() {
        return currIndex;
    }

    @Override
    public int size() {
        return dbCursor.size();
    }
}
