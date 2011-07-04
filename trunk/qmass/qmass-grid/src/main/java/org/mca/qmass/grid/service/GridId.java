package org.mca.qmass.grid.service;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 04.Tem.2011
 * Time: 11:00:55
 */
public class GridId implements Serializable {

    private Serializable var;

    private InetSocketAddress target;

    public GridId(Serializable var, InetSocketAddress target) {
        this.var = var;
        this.target = target;
    }

    public Serializable getVar() {
        return var;
    }

    public InetSocketAddress getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GridId gridId = (GridId) o;

        if (var != null ? !var.equals(gridId.var) : gridId.var != null) return false;
        if (target != null ? !target.equals(gridId.target) : gridId.target != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = var != null ? var.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GridId{" +
                "var=" + var +
                ", target=" + target +
                '}';
    }
}
