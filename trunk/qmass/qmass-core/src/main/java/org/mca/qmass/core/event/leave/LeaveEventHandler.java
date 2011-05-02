package org.mca.qmass.core.event.leave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 14:38:55
 */
public class LeaveEventHandler implements EventHandler {

    private static final Log logger = LogFactory.getLog(LeaveEventHandler.class);

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        LeaveEvent le = (LeaveEvent) event;
        LeaveService ls = (DefaultLeaveService) service;
        ls.removeFromCluster(new InetSocketAddress(le.getHostName(), le.getPort()));
        return this;
    }

}
