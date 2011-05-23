package org.mca.qmass.http.filters;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: malpay
 * Date: 23.May.2011
 * Time: 16:44:48
 * To change this template use File | Settings | File Templates.
 */
public class SessionAttributeTrackingRequestWrapper extends HttpServletRequestWrapper {

    public SessionAttributeTrackingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public HttpSession getSession() {
        return new AttributeTrackingSessionWrapper(super.getSession());
    }
}
