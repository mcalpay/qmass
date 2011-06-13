package org.mca.qmass.grid;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 13:19:44
 */
public class GridTests {

    @Test
    public void gridPutFar1CheckLocal2() throws Exception {
        InetSocketAddress server1 = new InetSocketAddress("localhost", 4444);
        InetSocketAddress server2 = new InetSocketAddress("localhost", 3333);
        Grid local1 = new LocalGrid();
        Grid far1 = new FarGrid(local1, server1, server2);
        Grid local2 = new LocalGrid();
        Grid far2 = new FarGrid(local2, server2, server1);
        far1.put(1L, 1L);
        Thread.sleep(1000);
        Assert.assertEquals(1L, local2.get(1L));
        far1.end();
        far2.end();
    }

    @Test
    public void gridPutLocal1ChekFar2() throws Exception {
        InetSocketAddress server1 = new InetSocketAddress("localhost", 4444);
        InetSocketAddress server2 = new InetSocketAddress("localhost", 3333);
        Grid local1 = new LocalGrid();
        Grid far1 = new FarGrid(local1, server1, server2);
        Grid local2 = new LocalGrid();
        Grid far2 = new FarGrid(local2, server2, server1);
        local1.put(1L, 1L);
        Thread.sleep(1000);
        Assert.assertEquals(1L, far2.get(1L));
        far1.end();
        far2.end();
    }

}
