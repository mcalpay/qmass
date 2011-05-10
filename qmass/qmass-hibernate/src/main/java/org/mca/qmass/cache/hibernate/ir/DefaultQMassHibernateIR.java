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
package org.mca.qmass.cache.hibernate.ir;

import org.mca.qmass.core.ir.DefaultQMassIR;

import java.util.Properties;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 13:49:51
 */
public class DefaultQMassHibernateIR extends DefaultQMassIR {

    private final Properties properties;

    public DefaultQMassHibernateIR(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean getReplicateUpdates() {
        String updates = (String) properties.get("qmass.replicate.updates");
        if (updates != null && !updates.isEmpty()) {
            return "true".equals(updates);
        }
        return super.getReplicateUpdates();
    }

    @Override
    public boolean getReplicateInserts() {
        String inserts = (String) properties.get("qmass.replicate.inserts");
        if (inserts != null && !inserts.isEmpty()) {
            return "true".equals(inserts);
        }
        return super.getReplicateInserts();
    }

    @Override
    public String getCluster() {
        String qc = (String) properties.get("qmass.cluster");
        if (qc != null && !qc.isEmpty()) {
            return qc;
        }
        return super.getCluster();
    }

    @Override
    public String getMulticastAddress() {
        String qc = (String) properties.get("qmass.multicast.cluster");
        if (qc != null && !qc.isEmpty()) {
            return qc;
        }
        return super.getMulticastAddress();
    }

    @Override
    public int getMulticastReadPort() {
        String port = (String) properties.get("qmass.multicast.readport");
        if (port != null && !port.isEmpty()) {
            return Integer.valueOf(port);
        }
        return super.getMulticastReadPort();
    }

    @Override
    public int getMulticastWritePort() {
        String port = (String) properties.get("qmass.multicast.writeport");
        if (port != null && !port.isEmpty()) {
            return Integer.valueOf(port);
        }
        return super.getMulticastWritePort();
    }
}
