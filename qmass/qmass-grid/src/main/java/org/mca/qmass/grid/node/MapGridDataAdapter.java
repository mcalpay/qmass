package org.mca.qmass.grid.node;

import org.mca.qmass.grid.Filter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void merge(Serializable key, Serializable value) {
        throw new RuntimeException("@TODO");
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

    @Override
    public Set<Map.Entry<Serializable, Serializable>> filter(Filter filter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
