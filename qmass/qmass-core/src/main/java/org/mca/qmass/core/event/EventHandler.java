package org.mca.qmass.core.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;

import java.nio.ByteBuffer;

/**
 * User: malpay
 * Date: 26.Nis.2011
 * Time: 11:18:35
 */
public interface EventHandler {

    EventHandler handleEvent(QMass qmass, Service service, ByteBuffer buffer);

}
