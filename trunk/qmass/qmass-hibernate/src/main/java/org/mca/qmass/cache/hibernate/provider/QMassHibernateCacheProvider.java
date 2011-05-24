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
import org.mca.qmass.core.ir.QMassIR;

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
        if (qname != null && !qname.isEmpty()) {
            IR.putIfDoesNotContain(qname, QMassIR.QMASS_IR, new DefaultQMassHibernateIR(properties));
            this.qmass = QMass.getQMass(qname);
        } else {
            properties.put("qmass.name",QMassIR.DEFAULT);
            IR.putIfDoesNotContain(QMassIR.DEFAULT, QMassIR.QMASS_IR, new DefaultQMassHibernateIR(properties));
            this.qmass = QMass.getQMass(QMassIR.DEFAULT);
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
