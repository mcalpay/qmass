package org.mca.qmass.cache;

import org.mca.qmass.core.QMass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 11:09:42
 */
public class DefaultQCache extends AbstractQCache {

    private Map dataMap = new ConcurrentHashMap();

    public DefaultQCache(Serializable id, QMass qmass, QCache parent, List<QCache> children) {
        super(id, qmass, parent, children);
    }

    @Override
    protected Map getDataMap() {
        return dataMap;
    }
}
