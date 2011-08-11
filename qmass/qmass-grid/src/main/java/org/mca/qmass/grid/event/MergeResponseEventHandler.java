package org.mca.qmass.grid.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;
import org.mca.qmass.grid.request.Response;
import org.mca.qmass.grid.service.GridService;

/**
 * User: malpay
 * Date: 10.08.2011
 * Time: 21:29
 */
public class MergeResponseEventHandler implements EventHandler {
    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        GridService gridService = (GridService) service;
        gridService.saveResponse((Response) event);
        return this;
    }
}
