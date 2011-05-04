package org.mca.qmass.cache.hibernate.factory;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.QueryResultsRegion;
import org.hibernate.cache.RegionFactory;
import org.hibernate.cache.Timestamper;
import org.hibernate.cache.TimestampsRegion;
import org.hibernate.cfg.Settings;
import org.mca.ir.IR;
import org.mca.qmass.cache.hibernate.ir.DefaultQMassHibernateIR;
import org.mca.qmass.core.QMass;

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
        IR.putIfDoesNotContain(qname, new DefaultQMassHibernateIR(properties));
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
