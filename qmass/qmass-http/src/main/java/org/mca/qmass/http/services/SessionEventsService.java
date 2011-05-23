package org.mca.qmass.http.services;

import org.mca.qmass.core.Service;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:15:43
 */
public interface SessionEventsService extends Service {

    SessionEventsService attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent);

    SessionEventsService attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent);

    SessionEventsService attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent);

    SessionEventsService doAttributeAdded(Serializable name, Serializable value);

    SessionEventsService doAttributeRemoved(Serializable name, Serializable value);

    SessionEventsService doAttributeReplaced(Serializable name, Serializable value);

    SessionEventsService sync(HttpSession session);
}
