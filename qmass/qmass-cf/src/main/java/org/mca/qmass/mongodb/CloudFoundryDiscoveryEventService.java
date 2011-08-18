package org.mca.qmass.mongodb;

import com.mongodb.DB;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.service.DiscoveryService;
import org.springframework.data.document.mongodb.MongoDbFactory;

/**
 * User: malpay
 * Date: 17.08.2011
 * Time: 22:40
 */
public class CloudFoundryDiscoveryEventService extends MongoDiscoveryEventService {
    public CloudFoundryDiscoveryEventService(QMass qmass, DiscoveryService discoveryService) {
        super(qmass, discoveryService);
    }

    @Override
    protected DB getDb(QMass qmass) {
        logger.debug("cloud env\n " + System.getenv());
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        MongoDbFactory f = new MongoServiceCreator(cloudEnvironment).createSingletonService().service;
        return f.getDb();
    }
}
