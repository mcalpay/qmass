package org.mca.qmass.core.event.greet;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 14:46:35
 */
public interface NodeGreetListener {
    NodeGreetListener greet(InetSocketAddress who);
}
