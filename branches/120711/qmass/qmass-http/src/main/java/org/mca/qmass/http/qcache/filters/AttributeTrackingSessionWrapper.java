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
package org.mca.qmass.http.qcache.filters;

import org.mca.qmass.http.ClusterAttributeFilter;
import org.mca.qmass.http.filters.AbstractAttributeFilteringHttpSessionWrapper;
import org.mca.qmass.http.filters.HttpSessionWrapper;
import org.mca.qmass.http.qcache.services.SessionEventsContext;
import org.mca.qmass.http.qcache.services.SessionEventsService;

import javax.servlet.http.HttpServletRequest;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 16:52:13
 * <p/>
 * It delegates to original session object while replicating the attribute changes.
 */
public class AttributeTrackingSessionWrapper extends AbstractAttributeFilteringHttpSessionWrapper {

    public AttributeTrackingSessionWrapper(HttpServletRequest request, ClusterAttributeFilter attributeFilter) {
        super(request, attributeFilter);
    }

    @Override
    protected Object doGet(String name) {
        return getSession().getAttribute(name);
    }

    @Override
    protected void doPut(String name, Object value) {
        getSession().setAttribute(name,value);
        getSessionEvents().attributeAdded(name, value);
    }

    @Override
    protected void doRemove(String name, Object value) {
        getSession().removeAttribute(name);
        getSessionEvents().attributeRemoved(name);
    }

    @Override
    protected void doInvalidate() {
        logger.debug("invalidate session.");
        SessionEventsContext.invalidate();
        getSession().invalidate();
    }

    private SessionEventsService getSessionEvents() {
        return SessionEventsContext.getCurrentInstance();
    }
}
