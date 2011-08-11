package org.mca.qmass.grid.node;

import java.io.Serializable;
import java.util.Map;

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

}
