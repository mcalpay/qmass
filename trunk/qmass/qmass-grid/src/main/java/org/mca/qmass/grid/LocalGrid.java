package org.mca.qmass.grid;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 14:26:07
 */
public class LocalGrid implements Grid {

    private Map<Serializable, Serializable> dataMap = new ConcurrentHashMap<Serializable, Serializable>();

    public Grid put(Serializable key, Serializable value) {
        dataMap.put(key, value);
        return this;
    }

    public Serializable get(Serializable key) {
        return dataMap.get(key);
    }

}
