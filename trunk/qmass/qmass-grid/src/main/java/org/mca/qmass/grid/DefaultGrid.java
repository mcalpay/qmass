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

import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.matcher.CurrentPrevGrid;
import org.mca.qmass.grid.matcher.GridKeyManager;
import org.mca.qmass.grid.node.GridNode;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.Serializable;
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

    protected final YALog log = YALogFactory.getLog(getClass());

    protected GridNode masterGridNode;

    private GridKeyManager gridKeyManager;

    protected QMass qmass;

    public DefaultGrid(GridNode masterGridNode, QMass qmass) {
        this.masterGridNode = masterGridNode;
        this.gridKeyManager = new GridKeyManager(masterGridNode);
        addGridNode(masterGridNode);
        this.qmass = qmass;
    }

    @Override
    public void merge(Serializable key, Serializable value) {
        throw new RuntimeException("@TODO");
    }


    public Boolean put(Serializable key, Serializable value) {
        CurrentPrevGrid cpg = gridKeyManager.newKey(key);
        GridNode prevNode = cpg.getPrev();
        GridNode currNode = cpg.getCurrent();

        if (!cpg.isCurrPrevEqual()) {
            log.warn("node mismatched for " + key + "\n\tmoving from " + prevNode + " to " + currNode);
            cpg.removePrev(key);
        }

        gridKeyManager.put(key, cpg.getCurrentIndex());
        return cpg.put(key, value);
    }

    public Serializable get(Serializable key) {
        CurrentPrevGrid cpg = gridKeyManager.newKey(key);
        if (!cpg.isCurrPrevEqual()) {
            Serializable val = cpg.removePrev(key);
            gridKeyManager.put(key, cpg.getCurrentIndex());
            if (val != null) {
                cpg.put(key, val);
                return val;
            }
        }
        return cpg.get(key);
    }

    @Override
    public Serializable remove(Serializable key) {
        CurrentPrevGrid cpg = gridKeyManager.newKey(key);
        gridKeyManager.remove(key);
        if (!cpg.isCurrPrevEqual()) {
            return cpg.removePrev(key);
        }
        return cpg.remove(key);
    }

    @Override
    public Set<Map.Entry<Serializable, Serializable>> filter(Filter filter) {
        Set<Map.Entry<Serializable, Serializable>> result = new HashSet<Map.Entry<Serializable, Serializable>>();
        List<GridNode> grid = gridKeyManager.getGrid();
        for (GridNode node : grid) {
            result.addAll(node.filter(filter));
        }

        return result;
    }

    @Override
    public GridNode end() {
        gridKeyManager.end();
        masterGridNode.end();
        masterGridNode = null;
        gridKeyManager = null;
        qmass = null;
        return this;
    }

    @Override
    public Grid addGridNode(final GridNode node) {
        gridKeyManager.addNode(node);
        return this;
    }

    @Override
    //@TODO May be improved with using more than one key map
    public Grid removeGridNode(GridNode node) {
        log.info("removeGridNode : " + node);
        gridKeyManager.removeNode(node);
        return this;
    }

    protected GridNode findNodeWithSocket(InetSocketAddress who) {
        return gridKeyManager.findNodeWithSocket(who);
    }

    @Override
    public int compareTo(Object o) {
        return new Integer(masterGridNode.hashCode()).compareTo(o.hashCode());
    }
}
