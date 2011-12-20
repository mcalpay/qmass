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
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.springframework.data.document.mongodb.MongoDbFactory;

/**
 * User: malpay
 * Date: 20.12.2011
 * Time: 11:18
 */
public class CloudFoundryMongoDBAccessor implements MongoDBAccessor {
    @Override
    public void testDB() {
    }

    @Override
    public DB getDB(String name) {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        MongoDbFactory f = new MongoServiceCreator(cloudEnvironment).createSingletonService().service;
        return f.getDb();
    }
}
