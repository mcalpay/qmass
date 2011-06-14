package org.mca.qmass.grid.matcher;

import org.mca.qmass.grid.GridNode;

import java.io.Serializable;
import java.util.List;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:27:28
 */
public interface KeyGridMatcher {

    GridNode match(Serializable key, List<GridNode> gridNodes);

}
