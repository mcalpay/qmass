package org.mca.qmass.grid;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 14.Haz.2011
 * Time: 09:36:02
 */
public class GridTests {

    @Test
    public void putGrid() throws Exception {
        Grid grid0 = new DefaultGrid();
        InetSocketAddress server0 = new InetSocketAddress("localhost", 4444);
        InetSocketAddress server1 = new InetSocketAddress("localhost", 3333);
        GridNode local0 = new LocalGridNode();
        GridNode far0 = new FarGridNode(local0, server0, server1);
        GridNode local1 = new LocalGridNode();
        GridNode far1 = new FarGridNode(local1, server1, server0);
        grid0.addGridNode(0, local0);
        grid0.addGridNode(1, far1);
        grid0.put(1L, 1L);
        grid0.put(2L, 2L);
        Assert.assertEquals(1L, grid0.get(1L));
        Assert.assertEquals(2L, grid0.get(2L));
        far0.end();
        far1.end();
    }

}
