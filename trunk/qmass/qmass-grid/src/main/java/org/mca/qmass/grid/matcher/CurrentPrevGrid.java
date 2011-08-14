package org.mca.qmass.grid.matcher;

import org.mca.qmass.grid.node.GridNode;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.08.2011
 * Time: 21:12
 */
public class CurrentPrevGrid {

    private GridNode prev;

    private GridNode current;

    private int currentIndex;

    private int prevIndex;

    private boolean currPrevEqual;

    public CurrentPrevGrid(GridNode prev, GridNode current, int prevIndex, int currentIndex) {
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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getPrevIndex() {
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
}
