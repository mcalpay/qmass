package org.mca.qmass.grid.matcher;

import org.mca.qmass.grid.GridMap;

import java.io.Serializable;
import java.util.List;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:27:28
 */
public interface KeyGridMatcher {

    GridMap match(Serializable key, List<GridMap> gridMaps);

}
