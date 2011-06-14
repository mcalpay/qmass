package org.mca.qmass.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.ir.IR;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.matcher.HashKeyGridMatcher;
import org.mca.qmass.grid.matcher.KeyGridMatcher;
import org.mca.qmass.grid.node.GridNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:01:07
 */
public class DefaultGrid implements Grid {

    protected final Log log = LogFactory.getLog(getClass());

    private List<GridNode> grid = new ArrayList<GridNode>();

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
    public GridNode end() {
        return this;
    }

    private GridNode getGrid(Serializable key) {
        GridNode gridNode = matcher.match(key, grid);
        log.debug("key " + key + " matched to : " + gridNode);
        return gridNode;
    }

    @Override
    public Grid addGridNode(int index, GridNode node) {
        grid.add(index, node);
        return this;
    }

    @Override
    public Grid removeGridNode(int index) {
        grid.remove(index);
        return this;
    }

    public static QMassGridIR getQMassGridIR() {
        return IR.get("default", "QMassGridIR");
    }

}
