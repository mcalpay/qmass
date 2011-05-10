package org.mca.qmass.cache.hibernate.provider;


import org.hibernate.cache.Cache;
import org.junit.Before;
import org.junit.Test;
import org.mca.ir.IR;
import org.mca.qmass.cache.hibernate.provider.QMassHibernateCacheProvider;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.ir.QMassIR;

import java.util.Properties;

import static junit.framework.Assert.*;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 15:16:09
 */
public class QMassHibernateCacheProviderTests {
     @Before
    public void configure() {
        IR.put(QMassIR.class, new DefaultQMassIR() {
            @Override
            public int getDefaultThreadWait() {
                return 100;
            }
        });
    }

    @Test
    public void removeFromOneQmassInstanceCheckIfItsRemovedFromOtherToo() throws Exception {
        QMassHibernateCacheProvider cp1 = new QMassHibernateCacheProvider(new QMass("test"));
        Cache c1 = cp1.buildCache("test",null);
        QMassHibernateCacheProvider cp2 = new QMassHibernateCacheProvider(new QMass("test"));
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
        Cache c1 = cp1.buildCache("test",null);
        QMassHibernateCacheProvider cp2 = new QMassHibernateCacheProvider(new QMass("test"));
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

    @Test
    public void propertiesAreSetThroughHibernate() throws Exception {
        QMassHibernateCacheProvider cp1 = new QMassHibernateCacheProvider();
        Properties props = new Properties();
        props.put("qmass.cluster", "localhost,6671,6671/");
        props.put("qmass.name", "hib1");
        cp1.start(props);
        assertEquals("hib1",cp1.qmass.getId());
        assertEquals(6671,((DatagramClusterManager)cp1.qmass.getClusterManager()).getListeningAt().getPort());
    }

}
