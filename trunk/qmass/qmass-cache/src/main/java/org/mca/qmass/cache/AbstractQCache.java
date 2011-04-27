package org.mca.qmass.cache;

import org.mca.qmass.cache.event.CacheRemoveEvent;
import org.mca.qmass.core.QMass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * s * User: malpay
 * Date: 27.Nis.2011
 * Time: 10:55:51
 */
public abstract class AbstractQCache implements QCache {

    private QCache parent;

    private List<QCache> children;

    private QMass qmass;

    private Serializable id;

    public AbstractQCache(Serializable id, QMass qmass, QCache parent, List<QCache> children) {
        this.id = id;
        this.qmass = qmass;
        this.parent = parent;
        this.children = children;
        this.qmass.registerService(this);
    }

    public Serializable getId() {
        return id;
    }

    @Override
    public QCache parent() {
        return parent;
    }

    @Override
    public List<QCache> children() {
        return children;
    }

    protected abstract Map getDataMap();

    @Override
    public Object get(Serializable key) {
        return getDataMap().get(key);
    }

    @Override
    public QCache put(Serializable key, Object value) {
        boolean update = false;
        if (getDataMap().containsKey(key)) {
            update = true;
        }
        getDataMap().put(key, value);
        if (update) {
            qmass.sendEvent(new CacheRemoveEvent(qmass.getId(), id, key));
        }
        return this;
    }

    @Override
    public QCache remove(Serializable key) {
        removeSilently(key);
        qmass.sendEvent(new CacheRemoveEvent(qmass.getId(), id, key));
        return this;
    }

    @Override
    public QCache removeSilently(Serializable key) {
        getDataMap().remove(key);
        return this;
    }

    @Override
    public QCache clear() {
        getDataMap().clear();
        for (QCache child : children) {
            child.clear();
        }
        qmass.sendEvent(new CacheRemoveEvent(qmass.getId(), id));
        return this;
    }

    @Override
    public QCache clearSilently() {
        getDataMap().clear();
        for (QCache child : children) {
            child.clearSilently();
        }
        return this;
    }
}
