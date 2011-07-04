package org.mca.qmass.http.grid;

import org.mca.qmass.grid.QMassGrid;
import org.mca.qmass.http.ClusterAttributeFilter;
import org.mca.qmass.http.filters.AbstractAttributeFilteringHttpSessionWrapper;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: malpay
 * Date: 20.Haz.2011
 * Time: 16:13:40
 */
public class GridSessionWrapper extends AbstractAttributeFilteringHttpSessionWrapper {

    private QMassGrid grid;

    private static final String SHAREDATTRIBUTES = "sharedattributes";

    public GridSessionWrapper(QMassGrid grid, HttpServletRequest request, ClusterAttributeFilter attributeFilter) {
        super(request, attributeFilter);
        this.grid = grid;
    }

    /**
     * update the changed attributes to grid
     */
    public void sync() {
        List atts = (List) grid.get(SHAREDATTRIBUTES);
        if(atts != null) {
            for(Object name : atts) {
                Serializable value = (Serializable) getSession().getAttribute((String) name);
                grid.put((Serializable) name, value);
            }
        }
    }

    @Override
    protected Object doGet(String name) {
        List atts = (List) grid.get(SHAREDATTRIBUTES);
        if (atts != null && atts.contains(name)) {
            Serializable serializable = grid.get(name);
            getSession().setAttribute(name,serializable);
            return serializable;
        } else {
            return getSession().getAttribute(name);
        }
    }

    @Override
    protected void doPut(String name, Object value) {
        grid.put(name, (Serializable) value);
        List atts = (List) grid.get(SHAREDATTRIBUTES);
        if (atts == null) {
            atts = new ArrayList();
        }

        atts.add(name);
        grid.put(SHAREDATTRIBUTES, (Serializable) atts);
        getSession().setAttribute(name,value);
    }

    @Override
    protected void doRemove(String name, Object value) {
        grid.remove(name);
        getSession().removeAttribute(name);
    }

    @Override
    protected void doInvalidate() {
    }

}
