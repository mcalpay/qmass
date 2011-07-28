package org.mca.qmass.core.cluster.service;

import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 27.Tem.2011
 * Time: 15:14:28
 */
public interface EventService {

    void sendEvent(Event event) throws IOException;

    void sendEvent(InetSocketAddress to, Event event) throws IOException;

    void receiveEventAndDo(EventClosure closure) throws Exception;

}
