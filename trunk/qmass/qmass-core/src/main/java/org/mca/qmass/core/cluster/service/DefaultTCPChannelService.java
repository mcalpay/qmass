package org.mca.qmass.core.cluster.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: malpay
 * Date: 29.Tem.2011
 * Time: 12:28:46
 */
public class DefaultTCPChannelService implements TCPChannelService {

    private final Log logger = LogFactory.getLog(getClass());

    private InetSocketAddress listening;

    private SocketScannerManager scannerManager;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private Map<InetSocketAddress, SocketChannel> connectedChannels;

    private Map<InetSocketAddress, SocketChannel> acceptedChannels;

    @Override
    public SocketChannel getConnectedChannel(InetSocketAddress to) {
        SocketChannel sc = connectedChannels.get(to);
        if (sc == null) {
            try {
                sc = SocketChannel.open(to);
                sc.configureBlocking(false);
                sc.finishConnect();
                connectedChannels.put(to, sc);
            } catch (IOException e) {
                logger.error("error opening channel", e);
                return null;
            }
        }
        return sc;
    }

    @Override
    public List<SocketChannel> getReadableSocketChannels() throws IOException {
        List<SocketChannel> channels = new ArrayList<SocketChannel>();
        selector.select();
        for (SelectionKey sk : selector.selectedKeys()) {
            if (sk.isAcceptable()) {
                SocketChannel sc = ((ServerSocketChannel) sk.channel()).accept();
                if (sc != null) {
                    sc.configureBlocking(false);
                    sc.finishConnect();
                    sc.register(selector, SelectionKey.OP_READ);
                    InetSocketAddress remoteSocket = (InetSocketAddress) sc.socket().getRemoteSocketAddress();
                    acceptedChannels.put(remoteSocket, sc);
                }
            } else if (sk.isReadable()) {
                SocketChannel sc = (SocketChannel) sk.channel();
                channels.add(sc);
            }
        }
        return channels;
    }

    @Override
    public InetSocketAddress getListening() {
        return listening;
    }

    @Override
    public void startListening() {
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
        while (socket != null) {
            try {
                this.serverSocketChannel.socket().bind(socket);
                listening = socket;
                break;
            } catch (Exception e) {
                socket = scanner.scan();
            }
        }

        if (listening == null) {
            throw new RuntimeException("Couldnt find a free port to listen!");
        }

    }

    @Override
    public Serializable getId() {
        return TCPChannelService.class;
    }
}
