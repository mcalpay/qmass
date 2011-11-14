package org.mca.qmass.grid.node;

import org.mca.qmass.grid.Filter;
import org.mca.qmass.persistence.Cursor;
import org.mca.qmass.persistence.FilterPredicate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: malpay
 * Date: 12.Tem.2011
 * Time: 17:33:31
 */
public interface GridData {

    void merge(Serializable key, Serializable value);

    Boolean put(Serializable key, Serializable value);

    Serializable get(Serializable key);

    Serializable remove(Serializable key);

    Set<Map.Entry<Serializable, Serializable>> filter(Filter filter);

}
