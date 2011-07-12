package org.mca.qmass.grid;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 12.Tem.2011
 * Time: 17:33:31
 */
public interface GridData {
    
    Boolean put(Serializable key, Serializable value);

    Serializable get(Serializable key);

    Serializable remove(Serializable key);
    
}
