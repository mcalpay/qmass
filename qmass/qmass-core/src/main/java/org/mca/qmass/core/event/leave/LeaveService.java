package org.mca.qmass.core.event.leave;

import org.mca.qmass.core.Service;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 14:43:17
 */
public interface LeaveService extends Service {
    
    DefaultLeaveService removeFromCluster(InetSocketAddress who);

    DefaultLeaveService leave();
}
