package org.mca.qmass.grid.node;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 14:24:57
 */
public interface GridNode extends Comparable {

    Boolean put(Serializable key, Serializable value);

    Serializable get(Serializable key);

    GridNode end();
}
