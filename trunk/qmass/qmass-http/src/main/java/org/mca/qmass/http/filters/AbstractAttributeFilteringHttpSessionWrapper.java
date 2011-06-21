package org.mca.qmass.http.filters;

import org.mca.qmass.http.ClusterAttributeFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * User: malpay
 * Date: 20.Haz.2011
 * Time: 16:16:45
 */
public abstract class AbstractAttributeFilteringHttpSessionWrapper extends HttpSessionWrapper {

    private ClusterAttributeFilter attributeFilter;

    public AbstractAttributeFilteringHttpSessionWrapper(HttpServletRequest request,
                                                        ClusterAttributeFilter attributeFilter) {
        super(request);
        this.attributeFilter = attributeFilter;
    }

    @Override
    public Object getAttribute(String name) {
        return doGet(name);
    }

    @Override
    public Object getValue(String name) {
        return doGet(name);
    }

    protected abstract Object doGet(String name);

    @Override
    public void setAttribute(String name, Object value) {
        if (!attributeFilter.filtered(name, value)) {
            logger.debug("cluster set attribute : " + name + "," + value);
            doPut(name, value);
        } else {
            getSession().setAttribute(name, value);
        }
    }

    protected abstract void doPut(String name, Object value);

    @Override
    public void putValue(String name, Object value) {
        if (!attributeFilter.filtered(name, value)) {
            logger.debug("cluster set attribute : " + name + "," + value);
            doPut(name, value);
        } else {
            getSession().putValue(name, value);
        }
    }


    @Override
    public void removeAttribute(String name) {
        Object value = getSession().getAttribute(name);
        if (!attributeFilter.filtered(name, value)) {
            logger.debug("cluster remove attribute : " + name);
            doRemove(name, value);
        } else {
            getSession().removeAttribute(name);
        }
    }

    protected abstract void doRemove(String name, Object value);

    @Override
    public void removeValue(String name) {
        Object value = getSession().getValue(name);
        if (!attributeFilter.filtered(name, value)) {
            logger.debug("cluster remove attribute : " + name);
            doRemove(name, value);
        } else {
            getSession().removeAttribute(name);
        }
    }

    @Override
    public void invalidate() {
        doInvalidate();
    }

    protected abstract void doInvalidate();

    public ClusterAttributeFilter getAttributeFilter() {
        return attributeFilter;
    }
}
