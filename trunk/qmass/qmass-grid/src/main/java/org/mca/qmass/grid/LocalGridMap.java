package org.mca.qmass.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 14:26:07
 */
public class LocalGridMap implements GridMap {

    protected final Log log = LogFactory.getLog(getClass());

    private Map<Serializable, Serializable> dataMap = new ConcurrentHashMap<Serializable, Serializable>();

    public Boolean put(Serializable key, Serializable value) {
        try {
            dataMap.put(key, value);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("error for key " + key + ", " + value, e);
            return Boolean.FALSE;
        }
    }

    public Serializable get(Serializable key) {
        return dataMap.get(key);
    }

    @Override
    public GridMap end() {
        return this;
    }

}
