package org.mca.qmass.grid.matcher;

import org.mca.qmass.grid.GridMap;

import java.io.Serializable;
import java.util.List;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:30:54
 */
public class HashKeyGridMatcher implements KeyGridMatcher {

    public GridMap match(Serializable key, List<GridMap> gridMaps) {
        return gridMaps.get(key.hashCode() % gridMaps.size());
    }
    
}
