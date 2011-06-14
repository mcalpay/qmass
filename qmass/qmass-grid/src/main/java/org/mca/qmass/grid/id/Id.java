package org.mca.qmass.grid.id;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 14.Haz.2011
 * Time: 15:19:52
 */
public class Id implements Serializable {

    private Integer id;

    private Serializable key;

    public Id(Integer id, Serializable key) {
        this.id = id;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Id ýd = (Id) o;

        if (!id.equals(ýd.id)) return false;
        if (!key.equals(ýd.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + key.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Id{" +
                "id=" + id +
                ", key=" + key +
                '}';
    }
}
