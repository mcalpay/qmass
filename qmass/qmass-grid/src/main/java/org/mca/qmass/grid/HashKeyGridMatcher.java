package org.mca.qmass.grid;

import java.io.Serializable;
import java.util.List;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:30:54
 */
public class HashKeyGridMatcher implements KeyGridMatcher {

    public Grid match(Serializable key, List<Grid> grids) {
        return grids.get(key.hashCode() % grids.size());
    }
    
}
