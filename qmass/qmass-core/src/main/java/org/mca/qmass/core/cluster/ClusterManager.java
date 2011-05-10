package org.mca.qmass.core.cluster;

import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 15:56:13
 */
public interface ClusterManager {

    ClusterManager sendEvent(Event event) throws IOException;

    ClusterManager receiveEventAndDo(EventClosure closure) throws Exception;

    ClusterManager end() throws IOException;

    ClusterManager start();

    Serializable getId();
}
