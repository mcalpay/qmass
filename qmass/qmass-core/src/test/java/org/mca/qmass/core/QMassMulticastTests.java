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

    private static final String ID = "multi";

    private MulticastClusterManager getClusterManager(QMass qmass) {
        return (MulticastClusterManager) qmass.getClusterManager();
    }

    @Before
    public void configure() {
        IR.put(new IRKey(ID, QMassIR.QMASS_IR), new DefaultQMassIR() {

            @Override
            public String getMulticastAddress() {
                return "230.0.0.1";
            }

        });
    }

    @Test
    public void canCreateAndAccessTheDefaultQMassInstance() throws Exception {
        assertSame(QMass.getQMass(ID), QMass.getQMass(ID));
        QMass.getQMass(ID).end();
    }

    @Test
    public void checkMulticastClusterIsUsed() throws Exception {
        assertSame(QMass.getQMass(ID), QMass.getQMass(ID));
        assertTrue(QMass.getQMass(ID).getClusterManager() instanceof MulticastClusterManager);
        QMass.getQMass(ID).end();
    }


}
