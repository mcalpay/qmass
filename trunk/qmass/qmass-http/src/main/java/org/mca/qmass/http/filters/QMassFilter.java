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
import org.mca.qmass.core.QMass;
import org.mca.qmass.http.services.DefaultSessionEventsService;
import org.mca.qmass.http.services.SessionEventsContext;
import org.mca.qmass.http.services.SessionEventsService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 10:21:41
 *
 * It matches the sessions initiated on the same browser using the cookie 'QMASSWEBID'
 * This cookie will hold the id of the first session that the user started on a server.
 * After the cookie is set it will first try to sync the session attributes, and than
 * wrap the original HttpSession so that session attribute changes could be replicated.
 * This filter will start the qmass if it's not already started.
 */
public class QMassFilter implements Filter {

    protected final Log logger = LogFactory.getLog(getClass());

    private static final String QMASSWEBID = "QMASSWEBID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        getQMass();
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            Cookie qmasswebcookie = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (QMASSWEBID.equals(cookie.getName())) {
                        qmasswebcookie = cookie;
                        break;
                    }
                }
            }

            if (qmasswebcookie == null) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                qmasswebcookie = new Cookie(QMASSWEBID, request.getSession().getId());
                qmasswebcookie.setPath("/");
                response.addCookie(qmasswebcookie);
            } else {
                logger.debug(request.getContextPath() + "; " + QMASSWEBID
                        + "; " + qmasswebcookie.getValue()
                        + ", domain ; " + qmasswebcookie.getDomain()
                        + ", path ; " + qmasswebcookie.getPath());
            }

            String qmassid = qmasswebcookie.getValue();
            QMass mass = getQMass();
            SessionEventsService ses = (SessionEventsService) mass.getService(qmassid);
            if (ses == null) {
                SessionEventsContext.setCurrentInstance(
                        new DefaultSessionEventsService(qmassid,
                                getQMass()));
            } else {
                SessionEventsContext.setCurrentInstance(ses);
                ses.sync(request.getSession());
            }
        }

        if (servletRequest instanceof HttpServletRequest) {
            filterChain.doFilter(new SessionAttributeTrackingRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
        } else {
            logger.warn("continuing without wrapping");
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    // @TODO qmass config...
    private QMass getQMass() {
        return QMass.getQMass();
    }

    @Override
    public void destroy() {
    }
}
