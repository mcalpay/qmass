package org.mca.qmass.core.event.greet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.cluster.DatagramClusterManager;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;
import org.mca.qmass.core.event.greet.GreetService;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 17:47:45
 */
public class GreetEventHandler implements EventHandler {

    private static final Log logger = LogFactory.getLog(QMass.class);

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        GreetEvent ge = (GreetEvent) event;
        GreetService gs = (GreetService) service;
        getClusterManager(qmass).addToCluster(ge.getListeningAt());
        gs.greetIfHeDoesntKnowMe(ge.getListeningAt(), ge.getCluster());
        return this;
    }

    private DatagramClusterManager getClusterManager(QMass qmass) {
        return (DatagramClusterManager) qmass.getClusterManager();
    }

    private InetSocketAddress extractSocket(ByteBuffer buffer) {
        byte b = buffer.get();
        StringBuilder hostb = new StringBuilder();
        while (b != ',') {
            b = buffer.get();
            if (b != ',') {
                hostb.append((char) b);
            }
        }

        StringBuilder portb = new StringBuilder();
        while (b != ')') {
            b = buffer.get();
            if (b != ')') {
                portb.append((char) b);
            }
        }

        return new InetSocketAddress(hostb.toString(), Integer.valueOf(portb.toString()));
    }
}
