package org.mca.qmass.core.cluster.service;

import org.mca.qmass.core.Service;

import java.nio.channels.DatagramChannel;

/**
 * User: malpay
 * Date: 29.Tem.2011
 * Time: 12:23:53
 */
public interface UDPChannelService extends ChannelService {

    DatagramChannel getDatagramChannel();

}
