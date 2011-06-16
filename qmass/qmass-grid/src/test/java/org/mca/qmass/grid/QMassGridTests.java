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
    public void putGrid() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        QMassGrid grid1 = new QMassGrid(q1);
        QMassGrid grid2 = new QMassGrid(q2);
        grid1.put(1L,1L);
        Assert.assertEquals(1L,grid2.get(1L));
    }
    
}
