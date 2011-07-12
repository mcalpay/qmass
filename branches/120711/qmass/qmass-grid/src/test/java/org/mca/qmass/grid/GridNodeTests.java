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
import org.mca.qmass.grid.ir.DefaultQMassGridIR;
import org.mca.qmass.grid.node.FarGridNode;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.node.LocalGridNode;

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
        GridNode local1 = new LocalGridNode(server1);
        GridNode far1 = new FarGridNode(DefaultQMassGridIR.instance(), local1, server1, server2);
        GridNode local2 = new LocalGridNode(server2);
        GridNode far2 = new FarGridNode(DefaultQMassGridIR.instance(), local2, server2, server1);
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
        GridNode local1 = new LocalGridNode(server1);
        GridNode far1 = new FarGridNode(DefaultQMassGridIR.instance(), local1, server1, server2);
        GridNode local2 = new LocalGridNode(server2);
        GridNode far2 = new FarGridNode(DefaultQMassGridIR.instance(), local2, server2, server1);
        local1.put(1L, 1L);
        Thread.sleep(100);
        Assert.assertEquals(1L, far2.get(1L));
        far1.end();
        far2.end();
    }

}
