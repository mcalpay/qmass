package org.mca.qmass.grid;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 14:24:57
 */
public interface Grid {

    Grid put(Serializable key, Serializable value);

    Serializable get(Serializable key);

    Grid end();
}
