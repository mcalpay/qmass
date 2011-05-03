package org.mca.qmass.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    protected final Log logger = LogFactory.getLog(getClass());

    private QCache parent;

    private List<QCache> children;

    protected QMass qmass;

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

    protected abstract Map<Serializable,Serializable> getDataMap();

    @Override
    public Object getSilently(Serializable key) {
        return getDataMap().get(key);
    }

    @Override
    public QCache put(Serializable key, Serializable value) {
        boolean update = false;
        if (getDataMap().containsKey(key)) {
            update = true;
        }
        getDataMap().put(key, value);
        if (update) {
            doOnUpdate(key, value);
        } else {
            doOnInsert(key, value);
        }

        return this;
    }

    protected QCache doOnInsert(Serializable key, Serializable value) {
        return this;
    }

    protected QCache doOnUpdate(Serializable key, Serializable value) {
        qmass.sendEvent(new CacheRemoveEvent(qmass, this, key));
        return this;
    }

    @Override
    public QCache putSilently(Serializable key, Serializable value) {
        getDataMap().put(key, value);
        return this;
    }

    @Override
    public QCache remove(Serializable key) {
        removeSilently(key);
        qmass.sendEvent(new CacheRemoveEvent(qmass, this, key));
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
        qmass.sendEvent(new CacheRemoveEvent(qmass, this));
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

    @Override
    public String toString() {
        return "AbstractQCache{" +
                "id=" + id +
                '}';
    }
}
