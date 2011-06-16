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
import org.mca.qmass.grid.ir.QMassGridIR;
import org.mca.qmass.grid.matcher.HashKeyGridMatcher;
import org.mca.qmass.grid.matcher.KeyGridMatcher;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.node.TargetSocket;

import java.io.Serializable;
import java.lang.annotation.Target;
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

    protected GridNode findNodeWithSocket(InetSocketAddress who) {
        for (GridNode node : grid) {
            TargetSocket socket = (TargetSocket) node;
            if (socket.getTargetSocket().equals(who)) {
                return node;
            }
        }
        return null;
    }

    public static QMassGridIR getQMassGridIR() {
        return IR.get("default", "QMassGridIR");
    }

    @Override
    public int compareTo(Object o) {
        return new Integer(masterGridNode.hashCode()).compareTo(o.hashCode());
    }
}
