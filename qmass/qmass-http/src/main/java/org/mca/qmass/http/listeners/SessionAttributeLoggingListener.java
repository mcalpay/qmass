package org.mca.qmass.http.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:00:04
 */
public class SessionAttributeLoggingListener implements HttpSessionAttributeListener {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.debug("attributeAdded; " + httpSessionBindingEvent.getName() + ", " + httpSessionBindingEvent.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.debug("attributeRemoved; " + httpSessionBindingEvent.getName() + ", " + httpSessionBindingEvent.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.debug("attributeReplaced; " + httpSessionBindingEvent.getName()
                + ", " + httpSessionBindingEvent.getSession().getAttribute(httpSessionBindingEvent.getName()));
    }

}
