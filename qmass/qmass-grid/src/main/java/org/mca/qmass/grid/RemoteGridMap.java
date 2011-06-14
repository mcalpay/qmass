package org.mca.qmass.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class RemoteGridMap implements GridMap {

    protected final Log log = LogFactory.getLog(getClass());

    private List<GridMap> gridClusterMap = new ArrayList<GridMap>();

    private KeyGridMatcher matcher = new HashKeyGridMatcher();

    public Boolean put(Serializable key, Serializable value) {
        try {
            return getGrid(key).put(key, value);
        } catch (Exception e) {
            log.error("put failed for : key " + key + ", " + value, e);
            return Boolean.FALSE;
        }
    }

    public Serializable get(Serializable key) {
        return getGrid(key).get(key);
    }

    @Override
    public GridMap end() {
        return this;
    }

    private GridMap getGrid(Serializable key) {
        return matcher.match(key, gridClusterMap);
    }
}
