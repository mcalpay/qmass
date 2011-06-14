package org.mca.qmass.grid.matcher;

import org.mca.qmass.grid.node.GridNode;

import java.io.Serializable;
import java.util.List;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:30:54
 */
public class HashKeyGridMatcher implements KeyGridMatcher {

    public GridNode match(Serializable key, List<GridNode> gridNodes) {
        return gridNodes.get(key.hashCode() % gridNodes.size());
    }
    
}
