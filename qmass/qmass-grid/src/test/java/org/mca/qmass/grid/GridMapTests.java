package org.mca.qmass.grid;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 13:19:44
 */
public class GridMapTests {

    @Test
    public void gridPutFar1CheckLocal2() throws Exception {
        InetSocketAddress server1 = new InetSocketAddress("localhost", 4444);
        InetSocketAddress server2 = new InetSocketAddress("localhost", 3333);
        GridMap local1 = new LocalGridMap();
        GridMap far1 = new FarGridMap(local1, server1, server2);
        GridMap local2 = new LocalGridMap();
        GridMap far2 = new FarGridMap(local2, server2, server1);
        far1.put(1L, 1L);
        Thread.sleep(100);
        Assert.assertEquals(1L, local2.get(1L));
        far1.end();
        far2.end();
    }

    @Test
    public void gridPutLocal1ChekFar2() throws Exception {
        InetSocketAddress server1 = new InetSocketAddress("localhost", 4444);
        InetSocketAddress server2 = new InetSocketAddress("localhost", 3333);
        GridMap local1 = new LocalGridMap();
        GridMap far1 = new FarGridMap(local1, server1, server2);
        GridMap local2 = new LocalGridMap();
        GridMap far2 = new FarGridMap(local2, server2, server1);
        local1.put(1L, 1L);
        Thread.sleep(100);
        Assert.assertEquals(1L, far2.get(1L));
        far1.end();
        far2.end();
    }

}
