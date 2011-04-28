package org.mca.qmass.cache.hibernate.provider;


import org.hibernate.cache.Cache;
import org.junit.Before;
import org.junit.Test;
import org.mca.ir.IR;
import org.mca.qmass.cache.hibernate.provider.QMassHibernateCacheProvider;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.ir.QMassIR;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 15:16:09
 */
public class QMassHibernateCacheProviderTests {
     @Before
    public void configure() {
        IR.putIR(QMassIR.class, new DefaultQMassIR() {
            @Override
            public int getDefaultThreadWait() {
                return 100;
            }
        });
    }

    @Test
    public void removeFromOneQmassInstanceCheckIfItsRemovedFromOtherToo() throws Exception {
        QMassHibernateCacheProvider cp1 = new QMassHibernateCacheProvider(new QMass("test"));
        cp1.start(null);
        Cache c1 = cp1.buildCache("test",null);
        QMassHibernateCacheProvider cp2 = new QMassHibernateCacheProvider(new QMass("test"));
        cp2.start(null);
        Cache c2 = cp2.buildCache("test",null);
        Thread.sleep(250);
        c1.put("1L","Test");
        c2.put("1L","Test");
        assertNotNull(c2.get("1L"));
        c1.remove("1L");
        Thread.sleep(250);
        assertNull(c2.get("1L"));
        cp1.stop();
        cp2.stop();
    }

    @Test
    public void clearFromOneQmassInstanceCheckIfOtherIsCleared() throws Exception {
        QMassHibernateCacheProvider cp1 = new QMassHibernateCacheProvider(new QMass("test"));
        cp1.start(null);
        Cache c1 = cp1.buildCache("test",null);
        QMassHibernateCacheProvider cp2 = new QMassHibernateCacheProvider(new QMass("test"));
        cp2.start(null);
        Cache c2 = cp2.buildCache("test",null);
        Thread.sleep(250);
        c1.put("1L","Test");
        c2.put("1L","Test");
        assertNotNull(c2.get("1L"));
        c1.clear();
        Thread.sleep(250);
        assertNull(c2.get("1L"));
        cp1.stop();
        cp2.stop();
    }
}
