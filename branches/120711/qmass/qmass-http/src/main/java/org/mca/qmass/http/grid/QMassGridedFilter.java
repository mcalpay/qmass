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
import org.mca.qmass.http.filters.AbstractQMassFilter;
import org.mca.qmass.http.filters.HttpSessionWrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * User: malpay
 * Date: 20.Haz.2011
 * Time: 15:12:02
 * <p/>
 * Grid based implementation
 *
 * @TODO does not regard the qmassweb cookie yet
 */
public class QMassGridedFilter extends AbstractQMassFilter {


    @Override
    public void doAfterChain(HttpServletRequest servletRequest, HttpSessionWrapper wrapper) {
        GridSessionWrapper gw = (GridSessionWrapper) wrapper;
        gw.sync();
    }


    // @TODO getQMass().getVar() + "/Grid" should be refactored
    @Override
    public HttpSessionWrapper wrapSession(HttpServletRequest servletRequest, Cookie qmasswebcookie) {
        String gridid = getQMass().getId() + "/Grid/" + qmasswebcookie.getValue();
        QMassGrid grid = (QMassGrid) getQMass().getService(gridid);
        if (grid == null) {
            grid = new QMassGrid(qmasswebcookie.getValue(),getQMass());
        }
        return new GridSessionWrapper(grid, servletRequest, getAttributeFilter());
    }

    @Override
    public void doBeforeChain(HttpServletRequest request, Cookie qmasswebcookie) {
    }
}
