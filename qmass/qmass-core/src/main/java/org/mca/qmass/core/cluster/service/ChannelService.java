package org.mca.qmass.core.cluster.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * User: malpay
 * Date: 27.Tem.2011
 * Time: 15:13:42
 */
public interface ChannelService {

    SocketChannel getConnectedChannel(InetSocketAddress to);

    SocketChannel[] getReadableSocketChannels();

    DatagramChannel getDatagramChannel();

    void listenForDatagrams();

    void listenForServerSockets();
}
