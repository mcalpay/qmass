package org.mca.qmass.core.cluster.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
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

    public DefaultTCPChannelService(SocketScannerManager scannerManager) {
        this.scannerManager = scannerManager;
        this.acceptedChannels = new HashMap<InetSocketAddress, SocketChannel>();
        this.connectedChannels = new HashMap<InetSocketAddress, SocketChannel>();
    }

    @Override
    public SocketChannel getConnectedChannel(InetSocketAddress to) {
        SocketChannel sc = connectedChannels.get(to);
        if (sc == null) {
            try {
                logger.debug(getListening() + " trying to connect to " + to);
                sc = SocketChannel.open(to);
                sc.configureBlocking(false);
                connectedChannels.put(to, sc);
                logger.info(getListening() + " connected to channel " + to);
            } catch (ConnectException e) {
                logger.debug(getListening() + " error connecting to channel " + to);
            } catch (IOException e) {
                logger.error(getListening() + " error connecting to channel " + to, e);
            }
        }

        logger.debug(getListening() + " channel " + sc + " to " + to);
        return sc;
    }

    @Override
    public List<SocketChannel> getReadableSocketChannels() throws IOException {
        List<SocketChannel> channels = new ArrayList<SocketChannel>();
        logger.trace(getListening() + " trying to select.");
        selector.select(1);
        logger.trace(getListening() + " selected.");
        for (SelectionKey sk : selector.selectedKeys()) {
            SocketChannel sc = null;
            if (sk.isAcceptable()) {
                sc = ((ServerSocketChannel) sk.channel()).accept();
                if (sc != null) {
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                    InetSocketAddress remoteSocket = (InetSocketAddress) sc.socket().getRemoteSocketAddress();
                    acceptedChannels.put(remoteSocket, sc);
                    logger.info(getListening() + " acceptable " + sc);
                }
            } else if (sk.isReadable()) {
                sc = (SocketChannel) sk.channel();
                logger.info(getListening() + " readable " + sc);
            }
            if (sc != null) {
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
            logger.debug("selector configured " + selector);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = scannerManager.scanLocalSocket();
        InetSocketAddress socket = scanner.scan();
        while (socket != null) {
            try {
                this.serverSocketChannel.socket().bind(socket);
                listening = socket;
                logger.info("\n\tlistening at @ " + getListening());
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
    public void end() throws IOException {
        for (SocketChannel sc : this.acceptedChannels.values()) {
            sc.close();
        }

        for (SocketChannel sc : this.connectedChannels.values()) {
            sc.close();
        }

        this.serverSocketChannel.close();
    }

}
