package org.mca.qmass.core.scanner;

import org.mca.qmass.core.scanner.DefaultScanner;
import org.mca.qmass.core.scanner.ExceptLocalPortScanner;
import org.mca.qmass.core.scanner.LocalScanner;
import org.mca.qmass.core.scanner.Scanner;

import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 14:29:55
 */
public class SocketScannerManager {

    private List<SocketRange> ranges = new ArrayList<SocketRange>();

    public SocketScannerManager(String hostname, Integer portScanStart, Integer portScanEnd) {
        addSockets(hostname, portScanStart, portScanEnd);
    }

    public SocketScannerManager(String cluster) {
        String [] first = cluster.split("/");
        int i = 0 ;
        while(i < first.length) {
            String [] second = first[i].split(",");
            addSockets(second[0], Integer.valueOf(second[1]), Integer.valueOf(second[2]));
            i++;
        }
    }

    public SocketScannerManager addSockets(String hostname, Integer portScanStart, Integer portScanEnd) {
        ranges.add(new SocketRange(hostname, portScanStart, portScanEnd));
        return this;
    }

    public Scanner scanSocket() {
        return new DefaultScanner(ranges.iterator());
    }

    public Scanner scanLocalSocket() {
        return new LocalScanner(ranges.iterator());
    }

    public Scanner scanSocketExceptLocalPort(int port) {
        return new ExceptLocalPortScanner(ranges.iterator(),port);
    }
}
