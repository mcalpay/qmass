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
        String id = "checkMulticastClusterIsUsed";
        assertSame(QMass.getQMass(id), QMass.getQMass(id));
        assertTrue(QMass.getQMass(id).getClusterManager() instanceof MulticastClusterManager);
        QMass.getQMass(id).end();
    }


}
