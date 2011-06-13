package org.mca.qmass.grid;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:56:12
 */
public class KeyValue implements Serializable {

    private Serializable key;

    private Serializable value;

    public KeyValue(Serializable key, Serializable value) {
        this.key = key;
        this.value = value;
    }

    public Serializable getKey() {
        return key;
    }

    public Serializable getValue() {
        return value;
    }
}
