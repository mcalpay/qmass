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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 16:44:48
 * <p/>
 * Wraps the HttpSession.
 */
public class SessionAttributeTrackingRequestWrapper extends HttpServletRequestWrapper {

    public SessionAttributeTrackingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public HttpSession getSession() {
        HttpSession session = super.getSession();
        if (session instanceof AttributeTrackingSessionWrapper) {
            return session;
        }
        return new AttributeTrackingSessionWrapper(session);
    }

    @Override
    public HttpSession getSession(boolean create) {
        HttpSession session = super.getSession();
        if (session instanceof AttributeTrackingSessionWrapper) {
            return session;
        }
        return new AttributeTrackingSessionWrapper(session);
    }

    @Override
    public ServletRequest getRequest() {
        return this;
    }
}
