package org.mca.qmass.core.event.leave;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 14:46:54
 */
public interface NodeLeaveListener {

    NodeLeaveListener leave(InetSocketAddress who);

}
