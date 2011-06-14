package org.mca.qmass.grid.id;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 14.Haz.2011
 * Time: 15:18:48
 */
public class DefaultIdGenerator implements IdGenerator {

    private Serializable key;

    private int current = 0;

    public DefaultIdGenerator(Serializable key) {
        this.key = key;
    }

    @Override
    public Serializable nextId() {
        current++;
        return new Id(current, key);
    }

}
