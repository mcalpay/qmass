package org.mca.qmass.mongodb;

import com.mongodb.DB;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.cloudfoundry.runtime.service.document.MongoServiceCreator;
import org.mca.qmass.core.IPUtil;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.service.DiscoveryService;
import org.springframework.data.document.mongodb.MongoDbFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
        logger.debug("cloud properties\n" + System.getenv());
        try {
            logger.debug("host name " + InetAddress.getLocalHost().getHostName() + ", "+IPUtil.getLocalIpAsString());
        } catch (UnknownHostException e) {
        }

        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        MongoDbFactory f = new MongoServiceCreator(cloudEnvironment).createSingletonService().service;
        return f.getDb();
    }
}
