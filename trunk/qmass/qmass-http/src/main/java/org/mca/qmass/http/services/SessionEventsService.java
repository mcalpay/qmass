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

    SessionEventsService doAttributeAdded(Serializable name, Serializable value);

    SessionEventsService doAttributeRemoved(Serializable name);

    SessionEventsService sync(HttpSession session);

    SessionEventsService attributeAdded(String name, Object value);

    SessionEventsService attributeRemoved(String name);
}
