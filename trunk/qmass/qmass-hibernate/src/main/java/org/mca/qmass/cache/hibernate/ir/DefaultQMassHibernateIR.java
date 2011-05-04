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
}
