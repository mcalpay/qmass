package org.mca.qmass.grid;

/**
 * User: malpay
 * Date: 14.Haz.2011
 * Time: 09:22:29
 */
public interface Grid extends GridNode {

    Grid addGridNode(int index, GridNode node);

    Grid removeGridNode(int index);

}
