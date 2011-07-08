/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        grid.end();
    }

}
