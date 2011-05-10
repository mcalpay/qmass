package org.mca.qmass.core;

import org.junit.Before;
import org.junit.Test;
import org.mca.ir.IR;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.cluster.MulticastClusterManager;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.ir.QMassIR;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 14:07:34
 */
public class QMassMulticastTests {
    private static final int DEFTHREADWAIT = 100;

    private MulticastClusterManager getClusterManager(QMass qmass) {
        return (MulticastClusterManager) qmass.getClusterManager();
    }

    @Before
    public void configure() {
        IR.put(QMassIR.class, new DefaultQMassIR() {

            @Override
            public String getMulticastAddress() {
                return "230.0.0.1";
            }

            @Override
            public int getDefaultThreadWait() {
                return DEFTHREADWAIT;
            }
        });
    }

    @Test
    public void canCreateAndAccessTheDefaultQMassInstance() throws Exception {
        assertSame(QMass.getQMass(), QMass.getQMass());
        QMass.getQMass().end();
    }

    @Test
    public void checkMulticastClusterIsUsed() throws Exception {
        String id = "Test1";
        assertSame(QMass.getQMass(id), QMass.getQMass(id));
        assertTrue(QMass.getQMass(id).getClusterManager() instanceof MulticastClusterManager);
        QMass.getQMass(id).end();
    }


}
