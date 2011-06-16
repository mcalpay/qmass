package org.mca.qmass.grid;

import org.mca.qmass.grid.node.GridNode;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 14.Haz.2011
 * Time: 09:22:29
 */
public interface Grid extends GridNode {

    Grid addGridNode(GridNode node);

    Grid removeGridNode(GridNode node);

}
