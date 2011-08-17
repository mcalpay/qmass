package org.mca.qmass.core.cluster.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
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

    private QMass qmass;

    public DefaultUDPChannelService(QMass qmass, SocketScannerManager scannerManager) {
        this.qmass = qmass;
        this.scannerManager = scannerManager;
    }

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

            if (qmass.getIR().getUseEphemeralPorts()) {
                listenOnAEphemeralPort();
            } else {
                listenOnAScannerPort();
            }
        }
    }

    private void listenOnAEphemeralPort() {
        try {
            this.datagramChannel.socket().bind(new InetSocketAddress("localhost", 0));
            listening = new InetSocketAddress(datagramChannel.socket().getInetAddress().getHostName(),
                    datagramChannel.socket().getLocalPort());
            logger.info("\n\tlistening at @ " + getListening());
        } catch (SocketException e) {
            throw new RuntimeException("Couldn't find a free port to listen!");
        } finally {
            try {
                this.datagramChannel.close();
            } catch (IOException e) {
                logger.error("error trying to close datagram channel", e);
            }
        }
    }

    private void listenOnAScannerPort() {
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
            logger.info("\n\tlistening at @ " + getListening());
        }
    }

    @Override
    public void end() throws IOException {
        //this.datagramChannel.socket().close();
        this.datagramChannel.close();
    }

}
