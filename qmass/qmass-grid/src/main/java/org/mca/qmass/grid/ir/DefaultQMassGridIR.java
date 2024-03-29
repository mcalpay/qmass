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
package org.mca.qmass.grid.ir;

import org.mca.qmass.mongodb.MongoDBAccessor;
import org.mca.qmass.mongodb.MongoDBAccessorImpl;
import org.mca.qmass.mongodb.MongoDBUtils;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 15:32:14
 */
public class DefaultQMassGridIR implements QMassGridIR {

    private static QMassGridIR instance;

    @Override
    public int getResponseTimeout() {
        return 3000;
    }

    @Override
    public boolean getWaitForPutResponse() {
        return true;
    }

    @Override
    public boolean getWaitForRemoveResponse() {
        return true;
    }

    @Override
    public String getMongoDBHost() {
        return "localhost";
    }

    @Override
    public MongoDBAccessor getMongoDBAccessor() {
        return new MongoDBAccessorImpl(getMongoDBHost());
    }

}
