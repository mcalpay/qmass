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

import com.mongodb.*;
import org.mca.qmass.core.utils.IPUtils;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.service.DiscoveryService;
import org.mca.qmass.core.cluster.service.UDPEventService;
import org.mca.qmass.core.scanner.Scanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * User: malpay
 * Date: 14.08.2011
 * Time: 17:39
 */
public class MongoDiscoveryEventService extends UDPEventService {

    private DBCollection collection;

    public MongoDiscoveryEventService(QMass qmass, DiscoveryService discoveryService) {
        super(qmass, discoveryService);
        DB db = getDb(qmass);
        collection = db.getCollection("qmass_discovery");
        DBObject listeningObj = new BasicDBObject().append("host", IPUtils.getLocalIpAsString())
                .append("port", getListening().getPort());
        if (collection.find(listeningObj).count() == 0) {
            collection.insert(listeningObj);
        }
    }

    protected DB getDb(QMass qmass) {
        MongoDBUtils mongoDBUtils = MongoDBUtils.instance();
        return mongoDBUtils.getDB(qmass.getId().toString());
    }

    @Override
    public void start() {
        final List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
        DBCursor curs = collection.find();
        while (curs.hasNext()) {
            DBObject obj = curs.next();
            InetSocketAddress sock = new InetSocketAddress((String) obj.get("host"), (Integer) obj.get("port"));
            logger.debug("db contains " + sock);
            if (!getListening().equals(sock)) {
                list.add(sock);
            }
        }
        this.greetService.setScanner(new Scanner() {
            Iterator<InetSocketAddress> iter = list.iterator();

            @Override
            public InetSocketAddress scan() {
                if (iter.hasNext()) {
                    return iter.next();
                }
                return null;
            }
        });
        super.start();
    }

    @Override
    public void end() throws IOException {
        DBObject listeningObj = new BasicDBObject().append("host", getListening().getHostName())
                .append("port", getListening().getPort());
        collection.remove(listeningObj);
        super.end();
    }

}
