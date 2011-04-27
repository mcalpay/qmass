package org.mca.qmass.core.scanner;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 15:31:15
 */
public class SocketRange {

    private InetAddress hostname;

    private Integer portScanStart;

    private Integer portScanEnd;

    private Integer portIndex;

    public SocketRange(String hostname, Integer portScanStart, Integer portScanEnd) {
        try {
            this.hostname = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.portScanStart = portScanStart;
        this.portIndex = portScanStart;
        this.portScanEnd = portScanEnd;
    }

    public InetSocketAddress nextSocket() {
        if (portIndex > portScanEnd) {
            return null;
        }

        InetSocketAddress address = new InetSocketAddress(hostname, portIndex);
        portIndex++;
        return address;
    }

    public boolean isLocal() {
        return this.hostname.isLoopbackAddress();
    }

    public void init() {
        this.portIndex = portScanStart;
    }
}