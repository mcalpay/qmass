package org.mca.qmass.core.cluster.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * User: malpay
 * Date: 28.Tem.2011
 * Time: 07:33:30
 */
public class DefaultChannelService implements ChannelService {

    protected final Log logger = LogFactory.getLog(getClass());

    private DatagramChannel datagramChannel;

    private SocketScannerManager scannerManager;

    private Selector selector;
    
    private ServerSocketChannel serverSocketChannel;

    @Override
    public SocketChannel getConnectedChannel(InetSocketAddress to) {
        return null;
    }

    @Override
    public SocketChannel[] getReadableSocketChannels() {
        return new SocketChannel[0];
    }


    @Override
    public void listenForServerSockets() {
        try {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = scannerManager.scanLocalSocket();
        InetSocketAddress socket = scanner.scan();
        InetSocketAddress listeningAt = null;
        while (socket != null) {
            try {
                this.serverSocketChannel.socket().bind(socket);
                listeningAt = socket;
                break;
            } catch (Exception e) {
                socket = scanner.scan();
            }
        }

        if (listeningAt == null) {
            throw new RuntimeException("Couldnt find a free port to listen!");
        }
    }

    @Override
    public DatagramChannel getDatagramChannel() {
        return datagramChannel;
    }

    @Override
    public void listenForDatagrams() {
        if (this.datagramChannel == null) {
            try {
                this.datagramChannel = DatagramChannel.open();
                this.datagramChannel.configureBlocking(false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Scanner scanner = scannerManager.scanLocalSocket();
            InetSocketAddress socket = scanner.scan();
            InetSocketAddress listeningAt = null;
            while (socket != null) {
                try {
                    this.datagramChannel.socket().bind(socket);
                    listeningAt = socket;
                    break;
                } catch (SocketException e) {
                    socket = scanner.scan();
                }
            }

            if (listeningAt == null) {
                try {
                    this.datagramChannel.close();
                } catch (IOException e) {
                    logger.error("error trying to close datagram channel", e);
                }
                this.datagramChannel = null;
                throw new RuntimeException("Couldn't find a free port to listen!");
            }
        }
    }

}
