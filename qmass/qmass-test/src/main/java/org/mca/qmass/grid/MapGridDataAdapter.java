package org.mca.qmass.grid;

import java.io.Serializable;
import java.util.Map;

/**
 * User: malpay
 * Date: 12.Tem.2011
 * Time: 17:36:40
 */
public class MapGridDataAdapter implements GridData {

    private Map delegate;

    public MapGridDataAdapter(Map delegate) {
        this.delegate = delegate;
    }

    @Override
    public Boolean put(Serializable key, Serializable value) {
        delegate.put(key, value);
        return true;
    }

    @Override
    public Serializable get(Serializable key) {
        return (Serializable) delegate.get(key);
    }

    @Override
    public Serializable remove(Serializable key) {
        return (Serializable) delegate.remove(key);
    }
}
