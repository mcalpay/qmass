package org.mca.qmass.cache;

import org.junit.Before;
import org.junit.Test;
import org.mca.ir.IR;
import org.mca.ir.IRKey;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.ClusterManager;
import org.mca.qmass.core.cluster.TCPClusterManager;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.ir.QMassIR;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * User: malpay
 * Date: 15.Tem.2011
 * Time: 13:41:52
 */
public class DefaultTCPQCacheTests {
    private static final String ID = "tcp";

    @Before
    public void configure() {
        IR.put(new IRKey(ID, QMassIR.QMASS_IR), new DefaultQMassIR() {

            @Override
            public ClusterManager newClusterManager(QMass q) {
                return new TCPClusterManager(q);
            }
        });
    }

    @Test
    public void removeFromOneQmassInstanceCheckIfItsRemovedFromOtherToo() throws Exception {
        QMass q1 = new QMass(ID);
        QMass q2 = new QMass(ID);
        QCache c1 = new DefaultQCache("cache", q1, null, null);
        QCache c2 = new DefaultQCache("cache", q2, null, null);
        c1.put("1L", "Test");
        c2.put("1L", "Test");
        assertNotNull(c2.getSilently("1L"));
        c1.remove("1L");
        Thread.sleep(250);
        assertNull(c2.getSilently("1L"));
        q1.end();
        q2.end();
    }

}
