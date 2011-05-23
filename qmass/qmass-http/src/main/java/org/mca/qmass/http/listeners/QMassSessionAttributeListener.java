package org.mca.qmass.http.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.http.QMassContext;
import org.mca.qmass.http.filters.QMassFilter;
import org.mca.qmass.http.services.SessionEventsContext;
import org.mca.qmass.http.services.SessionEventsService;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:00:04
 */
public class QMassSessionAttributeListener implements HttpSessionAttributeListener {

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.debug("attributeAdded; " + httpSessionBindingEvent.getName() + ", " + httpSessionBindingEvent.getValue());
        getSessionEvents().attributeAdded(httpSessionBindingEvent);
    }

    private SessionEventsService getSessionEvents() {
        return SessionEventsContext.getCurrentInstance();
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.debug("attributeRemoved; " + httpSessionBindingEvent.getName() + ", " + httpSessionBindingEvent.getValue());
        getSessionEvents().attributeRemoved(httpSessionBindingEvent);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.debug("attributeReplaced; " + httpSessionBindingEvent.getName()
                + ", " + httpSessionBindingEvent.getSession().getAttribute(httpSessionBindingEvent.getName()));
        getSessionEvents().attributeReplaced(httpSessionBindingEvent);
    }

}
