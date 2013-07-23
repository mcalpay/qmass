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
package org.mca.qmass.mongodb;

import com.mongodb.DB;
import org.mca.ir.IR;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: malpay
 * Date: 20.10.2011
 * Time: 11:10
 */
public class MongoDBUtils {

    private static MongoDBUtils instance;

    private MongoDBAccessor mongoDBAccessor;

    private Boolean isAvailable;

    private Map<String, DB> dbMap = new HashMap<String, DB>();

    public boolean isMongoAvailable() {
        // disable/enable the annoying test trace
        Logger mongoLogger = Logger.getLogger( "com.mongodb" );
        Level savedLevel = mongoLogger.getLevel();
        mongoLogger.setLevel(Level.SEVERE);
        if(isAvailable == null) {
            try {
                // test if a mongod is running
                mongoDBAccessor.testDB();
                isAvailable = Boolean.TRUE;
            } catch (Exception e) {
                isAvailable = Boolean.FALSE;
            }
        }
        mongoLogger.setLevel(savedLevel);
        return isAvailable;
    }

    public DB getDB(String name) {
        DB db = dbMap.get(name);
        if (db == null) {
            db = mongoDBAccessor.getDB(name);
            dbMap.put(name, db);
        }
        return db;
    }

    public static MongoDBUtils instance() {
        if(instance == null) {
            MongoDbIr mongoDbIr = IR.get("MongoDBIr");
            try {
                instance = new MongoDBUtils(mongoDbIr.getMongoDBAccessor());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return instance;
    }

    private MongoDBUtils(MongoDBAccessor accessor) {
        this.mongoDBAccessor = accessor;
    }
}
