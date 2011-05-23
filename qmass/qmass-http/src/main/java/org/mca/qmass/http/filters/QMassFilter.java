package org.mca.qmass.http.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 */
public class QMassFilter implements Filter {

    protected final Log logger = LogFactory.getLog(getClass());

    private static final String QMASSWEBID = "QMASSWEBID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
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
                Cookie cookie = new Cookie(QMASSWEBID, request.getSession().getId());
                cookie.setPath("/");
                response.addCookie(cookie);
            } else {
                logger.debug(request.getContextPath() + "; " + QMASSWEBID
                        + "; " + qmasswebcookie.getValue()
                        + ", domain ; " + qmasswebcookie.getDomain()
                        + ", path ; " + qmasswebcookie.getPath());
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {
    }
}
