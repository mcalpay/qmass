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

import com.mongodb.*;
import org.mca.qmass.core.serialization.JavaSerializationStrategy;
import org.mca.qmass.core.serialization.SerializationStrategy;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.Serializable;
import java.net.UnknownHostException;

/**
 * User: malpay
 * Date: 17.10.2011
 * Time: 11:03
 */
public class MongoDBTupleStore implements TupleStore {

    private final static YALog log = YALogFactory.getLog(MongoDBTupleStore.class);

    private DB db;

    private SerializationStrategy ss = new JavaSerializationStrategy();

    public MongoDBTupleStore() {
        db = MongoDBUtils.getDB("qmass");
    }

    @Override
    public Tuple get(Tuple tuple) {
        log.warn("get " + tuple);
        DBObject dbObj = new BasicDBObject();
        dbObj.put("key", tuple.getKey());
        DBObject found = db.getCollection(tuple.getType().toString()).findOne(dbObj);
        log.trace("found " + found);
        if (found != null) {
            Object value = ss.deSerialize((byte[]) found.get("value"));
            log.trace("returning get " + value);
            return (Tuple) value;
        }
        return null;
    }

    @Override
    public void persist(Tuple tuple) {
        DBObject dbObj = new BasicDBObject();
        dbObj.put("key", tuple.getKey());
        dbObj.put("value", ss.serialize(tuple));
        remove(tuple);
        DBCollection dbColl = db.getCollection(tuple.getType().toString());
        dbColl.save(dbObj);
        log.trace("persistet " + tuple);
    }

    @Override
    public void remove(Tuple tuple) {
        DBObject dbObj = new BasicDBObject();
        dbObj.put("key", tuple.getKey());
        db.getCollection(tuple.getType()).remove(dbObj);
        log.trace("removed " + tuple);
    }
}
