package org.mca.qmass.http.events;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;
import org.mca.qmass.http.services.SessionEventsService;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:42:02
 */
public class BindingEventHandler  implements EventHandler {
    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        SessionEventsService ses = (SessionEventsService) service;
        BindingEvent be = (BindingEvent) event;
        if(event instanceof AttributeAddEvent) {
            ses.doAttributeAdded(be.getName(),be.getValue());
        } else if(event instanceof AttributeRemoveEvent) {
            ses.doAttributeRemoved(be.getName(),be.getValue());
        } else if(event instanceof AttributeReplacedEvent) {
            ses.doAttributeReplaced(be.getName(),be.getValue());
        }

        return this; 
    }
}
