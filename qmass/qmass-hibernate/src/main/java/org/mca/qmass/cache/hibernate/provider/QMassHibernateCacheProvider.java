package org.mca.qmass.cache.hibernate.provider;

import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cache.Timestamper;
import org.mca.qmass.cache.hibernate.provider.HibernateCacheAdapter;
import org.mca.qmass.core.QMass;

import java.util.Properties;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 14:44:21
 */
public class QMassHibernateCacheProvider implements CacheProvider{

    private QMass qmass;

    QMassHibernateCacheProvider(QMass qmass){
        this.qmass = qmass;
    }


    public QMassHibernateCacheProvider(){
        qmass = QMass.getQMass();
    }

    @Override
    public Cache buildCache(String region, Properties properties) throws CacheException {
        return new HibernateCacheAdapter(region,qmass);
    }

    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }

    @Override
    public void start(Properties properties) throws CacheException {
    }

    @Override
    public void stop() {
        qmass.end();
    }

    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }
}
