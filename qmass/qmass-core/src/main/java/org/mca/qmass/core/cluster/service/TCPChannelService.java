package org.mca.qmass.core.cluster.service;

import org.mca.qmass.core.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * User: malpay
 * Date: 29.Tem.2011
 * Time: 12:23:10
 */
public interface TCPChannelService extends ChannelService {

    SocketChannel getConnectedChannel(InetSocketAddress to);

    List<SocketChannel> getReadableSocketChannels() throws IOException;

}
