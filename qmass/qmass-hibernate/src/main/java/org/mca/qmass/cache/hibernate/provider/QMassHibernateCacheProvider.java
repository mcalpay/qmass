package org.mca.qmass.cache.hibernate.provider;

import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cache.Timestamper;
import org.mca.ir.IR;
import org.mca.qmass.cache.hibernate.provider.HibernateCacheAdapter;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.ir.QMassIR;

import java.util.Properties;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 14:44:21
 */
public class QMassHibernateCacheProvider implements CacheProvider {

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
        IR.putIR(QMassIR.class, new DefaultQMassIR() {
            @Override
            public String getCluster() {
                String qc = (String) properties.get("qmass.cluster");
                if (qc != null && !qc.isEmpty()) {
                    return qc;
                }
                return super.getCluster();
            }
        });

        String qname = (String) properties.get("qmass.name");
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
