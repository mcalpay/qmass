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
package org.mca.qmass.web.ir;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.service.DiscoveryService;
import org.mca.qmass.core.cluster.service.EventService;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.http.SharedClusterAttributeFilter;
import org.mca.qmass.http.ClusterAttributeFilter;
import org.mca.qmass.http.ir.QMassHttpIR;
import org.mca.qmass.mongodb.CloudFoundryDiscoveryEventService;
import org.mca.qmass.mongodb.MongoDiscoveryEventService;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 24.May.2011
 * Time: 10:25:56
 * <p>Configure the qmass instance.</p>
 */
public class MyIR extends DefaultQMassIR implements QMassHttpIR {

    private ClusterAttributeFilter attributeFilter = new SharedClusterAttributeFilter();

    @Override
    public boolean getUseEphemeralPorts() {
        return true;
    }

    @Override
    public EventService getDiscoveryEventService(QMass qmass, DiscoveryService discoveryService, InetSocketAddress listening) {
        return new CloudFoundryDiscoveryEventService(qmass, discoveryService);
        //return super.getDiscoveryEventService(qmass,discoveryService,listening);
    }

    @Override
    public ClusterAttributeFilter getClusterAttributeFilter() {
        return attributeFilter;
    }
}
