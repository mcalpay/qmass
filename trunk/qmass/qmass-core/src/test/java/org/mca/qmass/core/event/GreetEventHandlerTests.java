package org.mca.qmass.core.event;

import org.junit.Test;

import static junit.framework.Assert.*;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.GreetEventHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;


/**
 * User: malpay
 * Date: 26.Nis.2011
 * Time: 15:56:14
 */
public class GreetEventHandlerTests {

    @Test
    public void handle() throws Exception {
        GreetEventHandler eh = new GreetEventHandler();
        byte[] bytes = "(localhost,6662)(localhost,6663)".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        eh.handleEvent(QMass.getQMass(),QMass.getQMass().getService(QMass.getQMass().getId() + "greet"), buffer);
        assertEquals(new InetSocketAddress("localhost", 6662), eh.who);
        assertEquals(1, eh.knowsWho.size());
        assertEquals(new InetSocketAddress("localhost", 6663), eh.knowsWho.get(0));
        QMass.getQMass().end();
    }
}
