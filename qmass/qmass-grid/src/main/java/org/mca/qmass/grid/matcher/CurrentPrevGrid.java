package org.mca.qmass.grid.matcher;

import org.mca.qmass.grid.node.GridNode;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 13.08.2011
 * Time: 21:12
 */
public class CurrentPrevGrid {

    private GridNode prev;

    private GridNode current;

    private InetSocketAddress currentIndex;

    private InetSocketAddress prevIndex;

    private boolean currPrevEqual;

    public CurrentPrevGrid(GridNode prev, GridNode current,
                           InetSocketAddress prevIndex, InetSocketAddress currentIndex) {
        this.prev = prev;
        this.current = current;
        this.currentIndex = currentIndex;
        this.prevIndex = prevIndex;
        currPrevEqual = current.equals(prev);
    }

    public GridNode getPrev() {
        return prev;
    }

    public GridNode getCurrent() {
        return current;
    }

    public InetSocketAddress getCurrentIndex() {
        return currentIndex;
    }

    public InetSocketAddress getPrevIndex() {
        return prevIndex;
    }

    public boolean isCurrPrevEqual() {
        return currPrevEqual;
    }

    public Boolean put(Serializable key, Serializable value) {
        return current.put(key, value);
    }

    public Serializable removePrev(Serializable key) {
        return prev.remove(key);
    }

    public Serializable get(Serializable key) {
        return current.get(key);
    }

    public Serializable remove(Serializable key) {
        return current.remove(key);
    }

    @Override
    public String toString() {
        return "CurrentPrevGrid{" +
                "prev=" + prev +
                ", current=" + current +
                ", currentIndex=" + currentIndex +
                ", prevIndex=" + prevIndex +
                ", currPrevEqual=" + currPrevEqual +
                '}';
    }
}
