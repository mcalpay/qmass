package org.mca.qmass.cache.hibernate.factory;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CollectionRegion;
import org.hibernate.cache.QueryResultsRegion;
import org.hibernate.cache.RegionFactory;
import org.hibernate.cache.Timestamper;
import org.hibernate.cache.TimestampsRegion;
import org.hibernate.cfg.Settings;
import org.mca.qmass.core.QMass;

import java.util.Properties;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 10:00:21
 */
public class QMassRegionFactory implements RegionFactory {

    private QMass qmass;

    @Override
    public void start(Settings settings, Properties properties) throws CacheException {
        this.qmass = QMass.getQMass();
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
    public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return null;
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
