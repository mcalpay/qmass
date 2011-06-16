package org.mca.qmass.grid.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;
import org.mca.qmass.grid.service.GridService;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 09:50:05
 */
public class GetRequestEventHandler implements EventHandler {

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        GridService gridService = (GridService) service;
        gridService.respondToGet((GetRequestEvent) event);
        return this;
    }

}
