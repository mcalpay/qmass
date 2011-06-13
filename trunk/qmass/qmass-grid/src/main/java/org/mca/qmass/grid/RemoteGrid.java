package org.mca.qmass.grid;

import org.mca.qmass.grid.matcher.HashKeyGridMatcher;
import org.mca.qmass.grid.matcher.KeyGridMatcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:01:07
 */
public class RemoteGrid implements Grid {

    private List<Grid> gridCluster = new ArrayList<Grid>();

    private KeyGridMatcher matcher = new HashKeyGridMatcher();

    public Grid put(Serializable key, Serializable value) {
        getGrid(key).put(key,value);
        return this;
    }

    public Serializable get(Serializable key) {
        return getGrid(key).get(key);
    }

    @Override
    public Grid end() {
        return this;
    }

    private Grid getGrid(Serializable key) {
        return matcher.match(key,gridCluster);
    }
}
