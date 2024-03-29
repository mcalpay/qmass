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
import org.mca.qmass.persistence.FilterPredicate;

import java.io.Serializable;
import java.util.Map;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 16:56:05
 */
public class QMassGridTests implements Serializable {

    @Test
    public void putGetOnGridWithNonDeterminedCluster() throws Exception {
        QMass q1 = new QMass("putGetOnGrid");
        QMassGrid grid1 = new QMassGrid(q1);
        QMass q2 = null;
        try {
            grid1.put(1L, 1L);
            Thread.sleep(1000);
            q2 = new QMass("putGetOnGrid");
            QMassGrid grid2 = new QMassGrid(q2);
            Thread.sleep(1000);
            Assert.assertEquals(1L, grid2.get(1L));
        } finally {
            q1.end();
            if (q2 != null) {
                q2.end();
            }
        }
    }

    public void putGetOnGrid() throws Exception {
        QMass q1 = new QMass("putGetOnGrid");
        QMass q2 = new QMass("putGetOnGrid");
        QMassGrid grid1 = new QMassGrid(q1);
        QMassGrid grid2 = new QMassGrid(q2);
        while (q1.getEventService().getCluster().length != 1
                || q2.getEventService().getCluster().length != 1) {
        }
        try {
            grid1.put(1L, 0L);
            grid1.put(1L, 1L);
            Assert.assertEquals(1L, grid2.get(1L));
            Assert.assertEquals(1L, grid1.get(1L));
        } finally {
            q1.end();
            q2.end();
        }
    }

    @Test
    public void putGetRemoveOnGrid() throws Exception {
        QMass q1 = new QMass("putGetRemoveOnGrid");
        QMass q2 = new QMass("putGetRemoveOnGrid");
        QMassGrid grid1 = new QMassGrid(q1);
        QMassGrid grid2 = new QMassGrid(q2);
        while (q1.getEventService().getCluster().length != 1
                || q2.getEventService().getCluster().length != 1) {
        }
        try {
            grid1.put(1L, 1L);
            Assert.assertEquals(1L, grid2.remove(1L));
            Assert.assertEquals(null, grid1.remove(1L));
        } finally {
            q1.end();
            q2.end();
        }
    }

    @Test
    public void createGridsWithDifferentVarNames() throws Exception {
        QMass q1 = new QMass("createGridsWithDifferentVarNames");
        QMass q2 = new QMass("createGridsWithDifferentVarNames");
        QMassGrid grid1 = new QMassGrid("q1", q1);
        QMassGrid grid2 = new QMassGrid("q2", q2);
        QMassGrid grid3 = new QMassGrid("q1", q2);
        while (q1.getEventService().getCluster().length != 1
                || q2.getEventService().getCluster().length != 1) {
        }
        try {
            grid1.put(1L, 1L);
            Assert.assertEquals(1L, grid1.get(1L));
            Assert.assertEquals(1L, grid3.get(1L));
            Assert.assertEquals(null, grid2.get(1L));
        } finally {
            q1.end();
            q2.end();
        }
    }

    @Test
    public void checkIfGridsGetCreatedWithPuts() throws Exception {
        QMass q1 = new QMass("test2");
        QMassGrid grid1 = new QMassGrid("x", q1);
        QMass q2 = new QMass("test2");
        while (q1.getEventService().getCluster().length != 1
                || q2.getEventService().getCluster().length != 1) {
        }
        try {
            grid1.put(1L, "murat");
            grid1.put(2L, "can");
            QMassGrid grid2 = (QMassGrid) q2.getService(QMassGrid.class + "/x");
            Assert.assertEquals("murat", grid2.get(1L));
            Assert.assertEquals("can", grid2.get(2L));
        } finally {
            q1.end();
            q2.end();
        }
    }

    @Test
    public void gridsGetEnded() throws Exception {
        QMass q2 = new QMass("testend");
        QMass q1 = new QMass("testend");
        QMassGrid grid1 = new QMassGrid("x", q1);
        while (q1.getEventService().getCluster().length != 1
                || q2.getEventService().getCluster().length != 1) {
        }
        try {
            grid1.put(1L, "murat");
            grid1.end();
            Assert.assertNull(q1.getService(QMassGrid.class + "/x"));
        } finally {
            q1.end();
            q2.end();
        }
    }

    @Test // @TODO 1
    public void addNewGridAfterPut() throws Exception {
        QMass q1 = new QMass("addNewGridAfterPut");
        QMassGrid grid1 = new QMassGrid("x", q1);
        QMass q2 = null;
        try {
            grid1.put(1L, "murat");
            grid1.put(2L, "can");
            q2 = new QMass("addNewGridAfterPut");
            QMassGrid grid2 = new QMassGrid("x", q2);
            Thread.sleep(1000);
            Assert.assertEquals("murat", grid1.get(1L));
            Assert.assertEquals("can", grid1.get(2L));
            Assert.assertEquals("murat", grid2.get(1L));
            Assert.assertEquals("can", grid2.get(2L));
        } finally {
            q1.end();
            if (q2 != null) {
                q2.end();
            }
        }
    }

    @Test // @TODO 2
    public void gridOfThree() throws Exception {
        QMass q1 = new QMass("gridOfThree");
        QMass q2 = new QMass("gridOfThree");
        QMass q3 = new QMass("gridOfThree");
        QMassGrid grid1 = new QMassGrid("x", q1);
        QMassGrid grid2 = new QMassGrid("x", q2);
        QMassGrid grid3 = new QMassGrid("x", q3);
        while (q1.getEventService().getCluster().length != 2
                || q2.getEventService().getCluster().length != 2
                || q3.getEventService().getCluster().length != 2) {
        }
        try {
            grid1.put(1L, "murat");
            grid1.put(2L, "can");
            grid1.put(3L, "alpay");
            Assert.assertEquals("can", grid1.get(2L));
            Assert.assertEquals("can", grid2.get(2L));
            Assert.assertEquals("can", grid3.get(2L));
        } finally {
            q1.end();
            q2.end();
            q3.end();
        }
    }

    @Test
    public void greedLeaves() throws Exception {
        QMass q1 = new QMass("greedLeaves");
        QMass q2 = new QMass("greedLeaves");
        QMassGrid grid1 = new QMassGrid("x", q1);
        new QMassGrid("x", q2);
        while (q1.getEventService().getCluster().length != 1
                || q2.getEventService().getCluster().length != 1) {
        }
        try {
            //Thread.sleep(1000);
            grid1.put(1L, "murat");
            q2.end();
            Thread.sleep(1000);
            Assert.assertEquals(null, grid1.get(1L));
        } finally {
            q1.end();
        }
    }

    @Test
    public void canFilterMaps() throws Exception {
        QMass q1 = new QMass("canFilterMaps");
        QMassGrid grid1 = new QMassGrid(q1);
        QMass q2 = new QMass("canFilterMaps");
        QMassGrid grid2 = new QMassGrid(q2);
        try {
            grid1.put(1L, 1L);
            grid1.put(2L, 2L);
            grid1.put(3L, 3L);
            grid1.put(4L, 4L);
            Thread.sleep(1000);
            Assert.assertEquals(3, grid2.filter(new FilterPredicate() {

                @Override
                public boolean filterInToResults(Object o) {
                    return (Long) o > 1L;
                }
            }).size());
        } finally {
            q1.end();
            q2.end();
        }
    }


}
