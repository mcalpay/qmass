package org.mca.qmass.core;

import org.junit.Before;
import org.junit.Test;
import org.mca.ir.IR;
import org.mca.ir.IRKey;
import org.mca.qmass.core.cluster.ClusterManager;
import org.mca.qmass.core.cluster.TCPClusterManager;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.ir.QMassIR;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

/**
 * User: malpay
 * Date: 14.Tem.2011
 * Time: 15:42:38
 */
public class QMassOverTCPTests {

    private static final String ID = "tcp";

    @Before
    public void configure() {
        IR.put(new IRKey("default", QMassIR.QMASS_IR), new DefaultQMassIR() {
            @Override
            public ClusterManager newClusterManager(QMass q) {
                return new TCPClusterManager(q);
            }
        });
    }

    @Test
    public void canCreateAndAccessTheQMassInstanceWithId() throws Exception {
        assertSame(QMass.getQMass(ID), QMass.getQMass(ID));
        QMass.getQMass(ID).end();
    }

    @Test
    public void twoInstanceGreetsEachOther() throws Exception {
        QMass mass1 = new QMass(ID);
        QMass mass2 = new QMass(ID);
        assertTrue(mass1 != mass2);
        Thread.sleep(5000);
        assertEquals(mass1.getClusterManager().getCluster()[0], mass2.getClusterManager().getListeningAt());
        assertEquals(mass2.getClusterManager().getCluster()[0], mass1.getClusterManager().getListeningAt());
        mass1.end();
        mass2.end();
    }

    @Test
    public void moreThanTwoInstanceGreetsEachOther() throws Exception {
        QMass mass1 = new QMass(ID);
        QMass mass2 = new QMass(ID);
        QMass mass3 = new QMass(ID);
        assertTrue(mass1 != mass2);
        assertTrue(mass2 != mass3);
        Thread.sleep(5000);
        assertEquals(mass1.getClusterManager().getCluster()[0], mass2.getClusterManager().getListeningAt());
        assertEquals(mass2.getClusterManager().getCluster()[0], mass1.getClusterManager().getListeningAt());
        assertEquals(2, mass1.getClusterManager().getCluster().length);
        mass1.end();
        mass2.end();
        mass3.end();
    }

    @Test
    public void twoInstanceOneLeaves() throws Exception {
        String id = "Test1";
        QMass mass1 = new QMass(id);
        QMass mass2 = new QMass(id);
        assertTrue(mass1 != mass2);
        Thread.sleep(1000);
        assertEquals(mass1.getClusterManager().getCluster()[0], mass2.getClusterManager().getListeningAt());
        assertEquals(mass2.getClusterManager().getCluster()[0], mass1.getClusterManager().getListeningAt());
        mass1.end();
        Thread.sleep(1000);
        assertEquals(0, mass2.getClusterManager().getCluster().length);
        mass2.end();
    }

}
