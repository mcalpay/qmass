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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * User: malpay
 * Date: 20.Haz.2011
 * Time: 15:42:31
 */
public class HttpSessionWrapper implements HttpSession {

    protected final Log logger = LogFactory.getLog(getClass());

    protected HttpServletRequest request;

    public HttpSessionWrapper(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public long getCreationTime() {
        return getSession().getCreationTime();
    }

    protected HttpSession getSession() {
        return request.getSession();
    }

    @Override
    public String getId() {
        return getSession().getId();
    }

    @Override
    public long getLastAccessedTime() {
        return getSession().getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return getSession().getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int i) {
        getSession().setMaxInactiveInterval(i);
    }

    @Override
    public int getMaxInactiveInterval() {
        return getSession().getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return getSession().getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        return getSession().getAttribute(name);
    }

    @Override
    public Object getValue(String name) {
        return getSession().getValue(name);
    }

    @Override
    public Enumeration getAttributeNames() {
        return getSession().getAttributeNames();
    }

    @Override
    public String[] getValueNames() {
        return getSession().getValueNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        getSession().setAttribute(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        getSession().putValue(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        getSession().removeAttribute(name);
    }

    @Override
    public void removeValue(String name) {
        getSession().removeValue(name);
    }

    @Override
    public void invalidate() {
        getSession().invalidate();
    }

    @Override
    public boolean isNew() {
        return getSession().isNew();
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}

