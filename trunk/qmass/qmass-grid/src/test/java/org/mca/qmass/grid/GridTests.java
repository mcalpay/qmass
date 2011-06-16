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
import org.mca.qmass.grid.node.FarGridNode;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.node.LocalGridNode;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 14.Haz.2011
 * Time: 09:36:02
 */
public class GridTests {

    @Test
    public void putGrid() throws Exception {
        InetSocketAddress server0 = new InetSocketAddress("localhost", 4444);
        InetSocketAddress server1 = new InetSocketAddress("localhost", 3333);
        GridNode local0 = new LocalGridNode(server0);
        Grid grid0 = new DefaultGrid(local0);
        GridNode far0 = new FarGridNode(local0, server0, server1);
        GridNode local1 = new LocalGridNode(server1);
        GridNode far1 = new FarGridNode(local1, server1, server0);
        grid0.addGridNode(far1);
        grid0.put(1L, 1L);
        grid0.put(2L, 2L);
        Assert.assertEquals(1L, grid0.get(1L));
        Assert.assertEquals(2L, grid0.get(2L));
        far0.end();
        far1.end();
    }

}
