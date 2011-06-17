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
package org.mca.qmass.http.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.http.ClusterAttributeFilter;
import org.mca.qmass.http.Shared;
import org.mca.qmass.http.services.SessionEventsContext;
import org.mca.qmass.http.services.SessionEventsService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 16:52:13
 * <p/>
 * It delegates to original session object while replicating the attribute changes.
 */
public class AttributeTrackingSessionWrapper implements HttpSession {

    private final Log logger = LogFactory.getLog(getClass());

    private HttpSession session;

    private ClusterAttributeFilter attributeFilter;

    public AttributeTrackingSessionWrapper(HttpSession session, ClusterAttributeFilter attributeFilter) {
        this.session = session;
        this.attributeFilter = attributeFilter;
    }

    @Override
    public long getCreationTime() {
        return session.getCreationTime();
    }

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public long getLastAccessedTime() {
        return session.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return session.getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int i) {
        session.setMaxInactiveInterval(i);
    }

    @Override
    public int getMaxInactiveInterval() {
        return session.getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return session.getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        return session.getAttribute(name);
    }

    @Override
    public Object getValue(String name) {
        return session.getValue(name);
    }

    @Override
    public Enumeration getAttributeNames() {
        return session.getAttributeNames();
    }

    @Override
    public String[] getValueNames() {
        return session.getValueNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        session.setAttribute(name, value);
        if (!attributeFilter.filtered(name, value)) {
            logger.debug("cluster set attribute : " + name + "," + value);
            getSessionEvents().attributeAdded(name, value);
        }
    }

    @Override
    public void putValue(String name, Object value) {
        session.putValue(name, value);
        if (!attributeFilter.filtered(name, value)) {
            logger.debug("cluster set attribute : " + name + "," + value);
            getSessionEvents().attributeAdded(name, value);
        }
    }


    @Override
    public void removeAttribute(String name) {
        Object value = session.getAttribute(name);
        session.removeAttribute(name);
        if (!attributeFilter.filtered(name,value)) {
            logger.debug("cluster remove attribute : " + name);
            getSessionEvents().attributeRemoved(name);
        }
    }

    @Override
    public void removeValue(String name) {
        Object value = session.getValue(name);
        session.removeValue(name);
        if (!attributeFilter.filtered(name,value)) {
            logger.debug("cluster remove attribute : " + name);
            getSessionEvents().attributeRemoved(name);
        }
    }

    @Override
    public void invalidate() {
        logger.debug("invalidate session.");
        SessionEventsContext.invalidate();
        session.invalidate();
    }

    @Override
    public boolean isNew() {
        return session.isNew();
    }

    private SessionEventsService getSessionEvents() {
        return SessionEventsContext.getCurrentInstance();
    }
}