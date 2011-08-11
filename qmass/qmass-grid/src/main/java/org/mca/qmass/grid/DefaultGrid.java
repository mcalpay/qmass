/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mca.qmass.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.ir.IR;
import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.matcher.HashKeyGridMatcher;
import org.mca.qmass.grid.matcher.KeyGridMatcher;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.node.LocalGridNode;
import org.mca.qmass.grid.node.TargetSocket;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:01:07
 *
 * @TODO List<GridNode> grid; propably must synced!, removeGridNode needs to sync key map and more testing
 */
public class DefaultGrid implements Grid {

    private static final String QMASS_KEY_MAP = "qmass.keyMap";

    protected final Log log = LogFactory.getLog(getClass());

    private List<GridNode> grid = new ArrayList<GridNode>();

    protected GridNode masterGridNode;

    private Map<Serializable, Integer> keyMap = new HashMap<Serializable, Integer>();

    private KeyGridMatcher matcher = new HashKeyGridMatcher();

    protected QMass qmass;

    public DefaultGrid(GridNode masterGridNode, QMass qmass) {
        this.masterGridNode = masterGridNode;
        masterGridNode.put(QMASS_KEY_MAP, (Serializable) keyMap);
        addGridNode(masterGridNode);
        this.qmass = qmass;
    }

    @Override
    public void merge(Serializable key, Serializable value) {
        throw new RuntimeException("@TODO");
    }

    public Boolean put(Serializable key, Serializable value) {
        Integer index = keyMap.get(key);
        GridNode prevNode;
        GridNode currNode = getGrid(key);
        Integer curIndex = grid.indexOf(currNode);
        if (index != null) {
            prevNode = grid.get(index);
        } else {
            prevNode = currNode;
        }

        if (!currNode.equals(prevNode)) {
            log.warn("node mismatched for " + key + "\n\tmoving from " + prevNode + " to " + currNode);
            prevNode.remove(key);
        }

        keyMap.put(key, curIndex);
        log.debug("node index " + curIndex + " keyMap " + keyMap);
        return currNode.put(key, value);
    }

    public Serializable get(Serializable key) {
        Integer index = keyMap.get(key);
        GridNode prevNode;
        GridNode currNode = getGrid(key);
        Integer curIndex = grid.indexOf(currNode);
        if (index != null) {
            prevNode = grid.get(index);
        } else {
            prevNode = currNode;
        }

        if (!currNode.equals(prevNode)) {
            Serializable val = prevNode.remove(key);
            log.warn("node mismatched for " + key + ", " + val + "\n\tmoving from " + prevNode + " to " + currNode);
            keyMap.put(key, curIndex);
            if (val != null) {
                currNode.put(key, val);
                return val;
            }
        }
        return currNode.get(key);
    }

    @Override
    public Serializable remove(Serializable key) {
        Integer index = keyMap.remove(key);
        GridNode prevNode;
        GridNode currNode = getGrid(key);
        if (index != null) {
            prevNode = grid.get(index);
        } else {
            prevNode = currNode;
        }

        if (!currNode.equals(prevNode)) {
            return prevNode.remove(key);
        }
        return currNode.remove(key);
    }

    @Override
    public GridNode end() {
        for (GridNode node : grid) {
            node.end();
        }
        grid = null;
        masterGridNode.end();
        masterGridNode = null;
        qmass = null;
        return this;
    }

    private GridNode getGrid(Serializable key) {
        GridNode gridNode = matcher.match(key, grid);
        log.debug(key + " matched to " + gridNode);
        return gridNode;
    }

    @Override
    public Grid addGridNode(final GridNode node) {
        grid.add(node);
        Collections.sort(grid);
        log.debug("nodes : " + grid);
        // @TODO thread pool or qmass send event runs on new thread
        if (!(node instanceof LocalGridNode) && !keyMap.isEmpty()) {
            log.debug("sync key map : " + keyMap);
            new Thread() {
                @Override
                public void run() {
                    node.merge(QMASS_KEY_MAP, (Serializable) keyMap);
                }
            }.start();
        }

        return this;
    }

    @Override
    public Grid removeGridNode(GridNode node) {
        grid.remove(node);
        Collections.sort(grid);
        log.debug("nodes : " + grid);
        return this;
    }

    protected GridNode findNodeWithSocket(InetSocketAddress who) {
        for (GridNode node : grid) {
            TargetSocket socket = (TargetSocket) node;
            if (socket.getTargetSocket().equals(who)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public int compareTo(Object o) {
        return new Integer(masterGridNode.hashCode()).compareTo(o.hashCode());
    }

}
