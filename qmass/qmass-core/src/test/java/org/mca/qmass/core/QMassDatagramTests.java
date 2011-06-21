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
package org.mca.qmass.core;

import org.junit.Before;
import org.junit.Test;
import org.mca.ir.IR;
import org.mca.ir.IRKey;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.ir.QMassIR;

import static junit.framework.Assert.*;

/**
 * User: malpay
 * Date: 26.Nis.2011
 * Time: 11:26:01
 */
public class QMassDatagramTests {
    private static final int DEFTHREADWAIT = 100;

    private DatagramClusterManager getClusterManager(QMass qmass) {
        return (DatagramClusterManager) qmass.getClusterManager();
    }

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
        IR.put(new IRKey(QMassIR.DEFAULT, QMassIR.QMASS_IR), new DefaultQMassIR() {
            @Override
            public int getDefaultThreadWait() {
                return DEFTHREADWAIT;
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
        assertEquals(getClusterManager(mass1).getCluster()[0], getClusterManager(mass2).getListeningAt());
        assertEquals(getClusterManager(mass2).getCluster()[0], getClusterManager(mass1).getListeningAt());
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
        assertEquals(getClusterManager(mass1).getCluster()[0], getClusterManager(mass2).getListeningAt());
        assertEquals(getClusterManager(mass2).getCluster()[0], getClusterManager(mass1).getListeningAt());
        mass1.end();
        Thread.sleep(200);
        assertEquals(0, getClusterManager(mass2).getCluster().length);
        mass2.end();
    }

    @Test
    public void canHaveDifferentPropertiesAndOverrideDefaults() throws Exception {
        IR.put(new IRKey("q1", QMassIR.QMASS_IR), new DefaultQMassIR() {
            @Override
            public int getDefaultThreadWait() {
                return 10;
            }
        });
        QMass def = QMass.getQMass();
        QMass mass1 = QMass.getQMass("q1");
        assertEquals(DEFTHREADWAIT, def.getIR().getDefaultThreadWait());
        assertEquals(10, mass1.getIR().getDefaultThreadWait());
        def.end();
        mass1.end();
    }

}
