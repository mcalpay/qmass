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
package org.mca.qmass.grid;

import org.junit.Assert;
import org.junit.Test;
import org.mca.qmass.core.QMass;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 16:56:05
 */
public class QMassGridTests {

    @Test
    public void putGetOnGrid() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        QMassGrid grid1 = new QMassGrid(q1);
        QMassGrid grid2 = new QMassGrid(q2);
        grid1.put(1L, 1L);
        Assert.assertEquals(1L, grid2.get(1L));
        Assert.assertEquals(1L, grid1.get(1L));
    }

    @Test
    public void putGetRemoveOnGrid() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        QMassGrid grid1 = new QMassGrid(q1);
        QMassGrid grid2 = new QMassGrid(q2);
        grid1.put(1L, 1L);
        Assert.assertEquals(1L, grid2.remove(1L));
        Assert.assertEquals(null, grid1.remove(1L));
    }

    @Test
    public void createGridsWithDifferentVarNames() throws Exception {
        QMass q1 = new QMass("test1");
        QMass q2 = new QMass("test1");
        QMassGrid grid1 = new QMassGrid("q1", q1);
        QMassGrid grid2 = new QMassGrid("q2", q2);
        QMassGrid grid3 = new QMassGrid("q1", q2);
        grid1.put(1L, 1L);
        Assert.assertEquals(1L, grid1.get(1L));
        Assert.assertEquals(1L, grid3.get(1L));
        Assert.assertEquals(null, grid2.get(1L));
    }

}
