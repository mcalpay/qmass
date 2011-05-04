package org.mca.qmass.cache.hibernate.factory;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.QueryResultsRegion;
import org.hibernate.cache.RegionFactory;
import org.hibernate.cache.Timestamper;
import org.hibernate.cache.TimestampsRegion;
import org.hibernate.cfg.Settings;
import org.mca.ir.IR;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.ir.DefaultQMassIR;

import java.util.Properties;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 10:00:21
 */
public class QMassRegionFactory implements RegionFactory {

    private Properties properties;

    private QMass qmass;

    public QMassRegionFactory(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void start(Settings settings, final Properties properties) throws CacheException {
        String qname = (String) properties.get("qmass.name");
        IR.put(qname, new DefaultQMassIR() {

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
        });
        ;
        if (qname != null && !qname.isEmpty()) {
            this.qmass = QMass.getQMass(qname);
        } else {
            this.qmass = QMass.getQMass();
        }
    }

    @Override
    public void stop() {
        qmass.end();
    }

    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }

    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }

    @Override
    public org.hibernate.cache.EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new EntityRegion(regionName, metadata, qmass);
    }

    @Override
    public org.hibernate.cache.CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new CollectionRegion(regionName, metadata, qmass);
    }

    @Override
    public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
        return new GeneralRegion(regionName, qmass);
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
        return new GeneralRegion(regionName, qmass);
    }
}
