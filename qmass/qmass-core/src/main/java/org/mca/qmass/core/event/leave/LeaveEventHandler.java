package org.mca.qmass.core.event.leave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.EventHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 14:38:55
 * To change this template use File | Settings | File Templates.
 */
public class LeaveEventHandler implements EventHandler {

    private static final Log logger = LogFactory.getLog(LeaveEventHandler.class);

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            InetSocketAddress who = extractSocket(buffer);
            logger.info(who + " leaves...");
            LeaveService ls = (DefaultLeaveService) service;
            ls.removeFromCluster(who);
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
