package org.mca.qmass.core.cluster.service;

import org.mca.qmass.core.Service;
import org.mca.qmass.core.cluster.EventManager;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 27.Tem.2011
 * Time: 15:14:28
 */
public interface EventService extends EventManager, DiscoveryService, Service {

    void sendEvent(Event event);

    void sendEvent(InetSocketAddress to, Event event);

    InetSocketAddress getListening();

    void start();

    void end() throws IOException;

}
