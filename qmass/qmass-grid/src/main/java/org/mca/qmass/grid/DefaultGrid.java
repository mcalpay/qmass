package org.mca.qmass.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.ir.IR;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.matcher.HashKeyGridMatcher;
import org.mca.qmass.grid.matcher.KeyGridMatcher;
import org.mca.qmass.grid.node.GridNode;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:01:07
 */
public class DefaultGrid implements Grid {

    protected final Log log = LogFactory.getLog(getClass());

    private List<GridNode> grid = new ArrayList<GridNode>();

    protected GridNode masterGridNode;

    private KeyGridMatcher matcher = new HashKeyGridMatcher();

    public DefaultGrid(GridNode masterGridNode) {
        this.masterGridNode = masterGridNode;
        addGridNode(masterGridNode);
    }

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
    public Grid addGridNode(GridNode node) {
        grid.add(node);
        Collections.sort(grid);
        return this;
    }

    @Override
    public Grid removeGridNode(GridNode node) {
        grid.remove(node);
        Collections.sort(grid);
        return this;
    }

    public static QMassGridIR getQMassGridIR() {
        return IR.get("default", "QMassGridIR");
    }

    @Override
    public int compareTo(Object o) {
        return new Integer(masterGridNode.hashCode()).compareTo(o.hashCode());
    }
}
