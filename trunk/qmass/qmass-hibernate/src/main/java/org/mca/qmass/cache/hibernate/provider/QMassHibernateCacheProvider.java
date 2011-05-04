package org.mca.qmass.cache.hibernate.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cache.Timestamper;
import org.mca.ir.IR;
import org.mca.qmass.cache.hibernate.ir.DefaultQMassHibernateIR;
import org.mca.qmass.cache.hibernate.provider.HibernateCacheAdapter;
import org.mca.qmass.core.QMass;

import java.util.Properties;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 14:44:21
 */
public class QMassHibernateCacheProvider implements CacheProvider {

    private final static Log logger = LogFactory.getLog(QMassHibernateCacheProvider.class);

    QMass qmass;

    QMassHibernateCacheProvider(QMass qmass) {
        this.qmass = qmass;
    }

    public QMassHibernateCacheProvider() {
    }

    @Override
    public Cache buildCache(String region, Properties properties) throws CacheException {
        return new HibernateCacheAdapter(region, qmass);
    }

    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }

    @Override
    public void start(final Properties properties) throws CacheException {
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
}
