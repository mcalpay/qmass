package org.mca.qmass.http.grid;

import org.mca.qmass.grid.QMassGrid;
import org.mca.qmass.http.ClusterAttributeFilter;
import org.mca.qmass.http.filters.AbstractAttributeFilteringHttpSessionWrapper;
import org.mca.qmass.http.filters.HttpSessionWrapper;
import org.mca.qmass.http.filters.SessionAttributeTrackingRequestWrapper;
import org.mca.qmass.http.qcache.services.SessionEventsContext;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 20.Haz.2011
 * Time: 16:13:40
 */
public class GridSessionWrapper extends AbstractAttributeFilteringHttpSessionWrapper {

    private QMassGrid grid;

    public GridSessionWrapper(QMassGrid grid, HttpServletRequest request, ClusterAttributeFilter attributeFilter) {
        super(request, attributeFilter);
        this.grid = grid;
    }

    @Override
    protected Object doGet(String name) {
        List atts = (List) grid.get("sharedattributes");
        if (atts != null && atts.contains(name)) {
            return grid.get(name);
        } else {
            return getSession().getAttribute(name);
        }
    }

    @Override
    protected void doPut(String name, Object value) {
        grid.put(name, (Serializable) value);
        List atts = (List) grid.get("sharedattributes");
        if (atts == null) {
            atts = new ArrayList();
        }

        atts.add(name);
        grid.put("sharedattributes", (Serializable) atts);
    }

    @Override
    protected void doRemove(String name, Object value) {
        grid.remove(name);
    }

    @Override
    protected void doInvalidate() {
    }
}
