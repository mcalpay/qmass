package org.mca.qmass.grid.matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.grid.node.GridNode;
import org.mca.qmass.grid.node.LocalGridNode;
import org.mca.qmass.grid.node.TargetSocket;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * User: malpay
 * Date: 12.08.2011
 * Time: 13:22
 * To encapsulate thread safety of grid and keyMap
 */
public class GridKeyManager {

    protected final Log log = LogFactory.getLog(getClass());

    public static final String QMASS_KEY_MAP = "qmass.keyMap";

    private List<GridNode> grid = new ArrayList<GridNode>();

    private Map<Serializable, InetSocketAddress> keyMap = new HashMap<Serializable, InetSocketAddress>();

    private KeyGridMatcher matcher = new HashKeyGridMatcher();

    public GridKeyManager(GridNode masterGridNode) {
        masterGridNode.put(QMASS_KEY_MAP, (Serializable) keyMap);
    }

    public List<GridNode> getGrid() {
        return grid;
    }

    public CurrentPrevGrid newKey(Serializable key) {
        InetSocketAddress prevIndex = keyMap.get(key);
        GridNode prevNode;
        GridNode currNode = getGrid(key);
        InetSocketAddress curIndex = ((TargetSocket)currNode).getTargetSocket();
        if (prevIndex != null) {
            prevNode = findNodeWithSocket(prevIndex);
        } else {
            prevIndex = curIndex;
            prevNode = currNode;
        }
        return new CurrentPrevGrid(prevNode, currNode, prevIndex, curIndex);
    }

    private GridNode getGrid(Serializable key) {
        GridNode gridNode = matcher.match(key, grid);
        log.debug(key + " matched to " + gridNode);
        return gridNode;
    }

    private GridNode getPrevGrid(Serializable key, GridNode curr) {
        InetSocketAddress index = keyMap.get(key);
        GridNode node = curr;
        if (index != null) {
            node = findNodeWithSocket(index);
        }
        return node;
    }

    public void addNode(final GridNode node) {
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
    }

    public void removeNode(GridNode node) {
        int index = grid.indexOf(node);
        if (index < 0) {
            log.info(node + " not found ");
            return;
        }

        Collection<Serializable> keys = keyMap.keySet();
        log.debug("keymap before remove " + index + "," + keyMap);
        for (Serializable key : keys) {
            InetSocketAddress i = keyMap.get(key);
            if (node.equals(i)) {
                keyMap.remove(key);
            }
        }

        grid.remove(node);
        Collections.sort(grid);
        log.debug("keymap after remove " + keyMap);
        log.debug("nodes : " + grid);
    }

    public GridNode findNodeWithSocket(InetSocketAddress who) {
        for (GridNode node : grid) {
            TargetSocket socket = (TargetSocket) node;
            if (socket.getTargetSocket().equals(who)) {
                return node;
            }
        }
        return null;
    }

    public void end() {
        for (GridNode node : grid) {
            node.end();
        }
    }

    public void put(Serializable key, InetSocketAddress currentIndex) {
        keyMap.put(key, currentIndex);
    }

    public void remove(Serializable key) {
        keyMap.remove(key);
    }

}
