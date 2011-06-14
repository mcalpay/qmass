package org.mca.qmass.grid;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 13:19:44
 */
public class GridNodeTests {

    @Test
    public void gridPutFar1CheckLocal2() throws Exception {
        InetSocketAddress server1 = new InetSocketAddress("localhost", 4444);
        InetSocketAddress server2 = new InetSocketAddress("localhost", 3333);
        GridNode local1 = new LocalGridNode();
        GridNode far1 = new FarGridNode(local1, server1, server2);
        GridNode local2 = new LocalGridNode();
        GridNode far2 = new FarGridNode(local2, server2, server1);
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
        GridNode local1 = new LocalGridNode();
        GridNode far1 = new FarGridNode(local1, server1, server2);
        GridNode local2 = new LocalGridNode();
        GridNode far2 = new FarGridNode(local2, server2, server1);
        local1.put(1L, 1L);
        Thread.sleep(100);
        Assert.assertEquals(1L, far2.get(1L));
        far1.end();
        far2.end();
    }

}
