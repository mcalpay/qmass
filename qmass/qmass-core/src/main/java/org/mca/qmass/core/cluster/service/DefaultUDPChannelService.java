package org.mca.qmass.core.cluster.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;

/**
 * User: malpay
 * Date: 29.Tem.2011
 * Time: 12:32:54
 */
public class DefaultUDPChannelService implements UDPChannelService {

    private final Log logger = LogFactory.getLog(getClass());

    private SocketScannerManager scannerManager;

    private DatagramChannel datagramChannel;

    private InetSocketAddress listening;

    @Override
    public DatagramChannel getDatagramChannel() {
        return datagramChannel;
    }

    @Override
    public InetSocketAddress getListening() {
        return listening;
    }

    @Override
    public void startListening() {
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

            if (listening == null) {
                listening = listeningAt;
            }
        }
    }

    @Override
    public Serializable getId() {
        return UDPChannelService.class;
    }
}
