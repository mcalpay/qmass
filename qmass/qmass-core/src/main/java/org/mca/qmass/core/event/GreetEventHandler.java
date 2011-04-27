package org.mca.qmass.core.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;

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

    InetSocketAddress who;

    List<InetSocketAddress> knowsWho = new ArrayList<InetSocketAddress>();

    @Override
    public EventHandler handleEvent(QMass qmass, ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            who = extractSocket(buffer);
            while (buffer.hasRemaining()) {
                knowsWho.add(extractSocket(buffer));
            }
            qmass.addSocketToCluster(who).greetIfHeDoesntKnowMe(who, knowsWho);
        } else {
            throw new RuntimeException("No who part");
        }
        return this;
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
