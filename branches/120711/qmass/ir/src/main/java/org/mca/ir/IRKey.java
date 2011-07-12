package org.mca.ir;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 20.Haz.2011
 * Time: 13:00:48
 */
public final class IRKey {

    private Serializable id;

    private Serializable type;

    public IRKey(Serializable id, Serializable type) {
        this.id = id;
        this.type = type;
    }

    public Serializable getId() {
        return id;
    }

    public Serializable getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IRKey irKey = (IRKey) o;

        if (!id.equals(irKey.id)) return false;
        if (!type.equals(irKey.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
