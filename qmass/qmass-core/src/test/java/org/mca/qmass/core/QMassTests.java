package org.mca.qmass.core;

import org.junit.Before;
import org.junit.Test;
import org.mca.ir.IR;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.ir.QMassIR;

import static junit.framework.Assert.*;

/**
 * User: malpay
 * Date: 26.Nis.2011
 * Time: 11:26:01
 */
public class QMassTests {

    @Test
    public void canAllocateUptoTenPortsWithDefaults() throws Exception {
        String id = "Test";
        int i = 10;
        while (i > 0) {
            QMass.getQMass(id + i);
            i--;
        }

        i = 10;
        while (i > 0) {
            QMass.getQMass(id + i).end();
            i--;
        }
    }

    @Test
    public void canCreateAndAccessTheDefaultQMassInstance() throws Exception {
        assertSame(QMass.getQMass(), QMass.getQMass());
        QMass.getQMass().end();
    }

    @Test
    public void canCreateAndAccessTheQMassInstanceWithId() throws Exception {
        String id = "Test1";
        assertSame(QMass.getQMass(id), QMass.getQMass(id));
        QMass.getQMass(id).end();
    }

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
    public void twoInstanceGreetsEachOther() throws Exception {
        String id = "Test1";
        QMass mass1 = new QMass(id);
        QMass mass2 = new QMass(id);
        assertTrue(mass1 != mass2);
        Thread.sleep(1000);
        assertTrue(mass1.cluster.contains(mass2.listeningAt));
        assertTrue(mass2.cluster.contains(mass1.listeningAt));
        mass1.end();
        mass2.end();
    }

    @Test
    public void twoInstanceOneLeaves() throws Exception {
        String id = "Test1";
        QMass mass1 = new QMass(id);
        QMass mass2 = new QMass(id);
        assertTrue(mass1 != mass2);
        Thread.sleep(1000);
        assertTrue(mass1.cluster.contains(mass2.listeningAt));
        assertTrue(mass2.cluster.contains(mass1.listeningAt));
        mass1.end();
        Thread.sleep(200);
        assertEquals(0, mass2.getCluster().length);
        mass2.end();
    }

}
