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
package org.mca.qmass.cache;

import org.junit.Before;
import org.junit.Test;
import org.mca.ir.IR;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.ir.QMassIR;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 14:17:25
 */
public class DefaultMulticastQCacheTests {

    @Before
    public void configure() {
        IR.put(QMassIR.class, new DefaultQMassIR() {

            @Override
            public String getMulticastAddress() {
                return "230.0.0.1";
            }

            @Override
            public int getDefaultThreadWait() {
                return 100;
            }
        });
    }

    @Test
    public void removeFromOneQmassInstanceCheckIfItsRemovedFromOtherToo() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        Thread.sleep(250);
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

    @Test
    public void clearFromOneQmassInstanceCheckIfOtherIsCleared() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        Thread.sleep(250);
        QCache c1 = new DefaultQCache("cache", q1, null, new ArrayList());
        QCache c2 = new DefaultQCache("cache", q2, null, new ArrayList());
        c1.put("1L", "Test");
        c2.put("1L", "Test");
        assertNotNull(c2.getSilently("1L"));
        c1.clear();
        Thread.sleep(250);
        assertNull(c2.getSilently("1L"));
        q1.end();
        q2.end();
    }

    @Test
    public void updateFromOneQmassInstanceCheckIfOtherIsCleared() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        Thread.sleep(250);
        QCache c1 = new DefaultQCache("cache", q1, null, new ArrayList());
        QCache c2 = new DefaultQCache("cache", q2, null, new ArrayList());
        c1.put("1L", "Test");
        c2.put("1L", "Test");
        assertNotNull(c2.getSilently("1L"));
        c1.put("1L", "Test1");
        Thread.sleep(250);
        assertNull(c2.getSilently("1L"));
        q1.end();
        q2.end();
    }

    @Test
    public void updateFromOneQmassInstanceCheckIfOtherIsUpdatedOnAReplicatedCache() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        Thread.sleep(250);
        QCache c1 = new ReplicatedQCache("cache", q1, null, new ArrayList(), true, true);
        QCache c2 = new ReplicatedQCache("cache", q2, null, new ArrayList(), true, true);
        c1.put("1L", "Test");
        c2.put("1L", "Test");
        assertNotNull(c2.getSilently("1L"));

        //TODO if removed test fails !
        Thread.sleep(250);
        c1.put("1L", "Test1");
        Thread.sleep(250);
        assertEquals(c1.getSilently("1L"), c2.getSilently("1L"));
        q1.end();
        q2.end();
    }

    @Test
    public void insertFromOneQmassInstanceCheckIfOtherIsUpdatedOnAReplicatedCache() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        Thread.sleep(250);
        QCache c1 = new ReplicatedQCache("cache", q1, null, new ArrayList(), true, true);
        QCache c2 = new ReplicatedQCache("cache", q2, null, new ArrayList(), true, true);
        c1.put("1L", "Test");
        Thread.sleep(250);
        assertNotNull(c2.getSilently("1L"));
        q1.end();
        q2.end();
    }

}
