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

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.ir.QMassIR;
import org.mca.qmass.http.ClusterAttributeFilter;
import org.mca.qmass.http.SharedClusterAttributeFilter;
import org.mca.qmass.http.ir.QMassHttpIR;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

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
 * <p/>
 * It matches the sessions initiated on the same browser using the cookie 'QMASSWEBID'
 * This cookie will hold the id of the first session that the user started on a server.
 * After the cookie is set it will first try to sync the session attributes, and than
 * wrap the original HttpSession so that session attribute changes could be replicated.
 * This filter will start the qmass if it's not already started.
 */
public abstract class AbstractQMassFilter implements Filter {

    protected final YALog logger = YALogFactory.getLog(getClass());

    private static final String QMASSWEBID = "QMASSWEBID";

    private String qmassid;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Object attribute = filterConfig.getServletContext().getInitParameter("qmass.name");
        this.qmassid = (attribute != null)
                ? (String) attribute : "default";
        getQMass();
        onInit();
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

            doBeforeChain(request, qmasswebcookie);
            HttpSessionWrapper sessionWrapper = wrapSession((HttpServletRequest) servletRequest, qmasswebcookie);
            filterChain.doFilter(
                    new SessionAttributeTrackingRequestWrapper(
                            sessionWrapper), servletResponse);
            doAfterChain((HttpServletRequest) servletRequest, sessionWrapper);
        } else {
            logger.warn("continuing without wrapping");
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    protected ClusterAttributeFilter getAttributeFilter() {
        QMassIR massIR = getQMass().getIR();
        if (massIR instanceof QMassHttpIR) {
            return ((QMassHttpIR) massIR).getClusterAttributeFilter();
        }
        return new SharedClusterAttributeFilter();
    }

    protected QMass getQMass() {
        return QMass.getQMass(qmassid);
    }

    @Override
    public void destroy() {
    }

    protected void onInit() {
    }

    public abstract void doAfterChain(HttpServletRequest servletRequest, HttpSessionWrapper wrapper);

    public abstract HttpSessionWrapper wrapSession(HttpServletRequest servletRequest, Cookie qmasswebcookie);

    public abstract void doBeforeChain(HttpServletRequest request, Cookie qmasswebcookie);

}
