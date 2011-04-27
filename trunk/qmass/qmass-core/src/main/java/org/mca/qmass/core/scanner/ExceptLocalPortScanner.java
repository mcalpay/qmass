package org.mca.qmass.core.scanner;

import org.mca.qmass.core.scanner.SocketRange;

import java.net.InetSocketAddress;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 17:16:58
 * To change this template use File | Settings | File Templates.
 */
public class ExceptLocalPortScanner extends AbstractScanner {

    private int port;

    public ExceptLocalPortScanner(Iterator<SocketRange> rangesIter, int port) {
        super(rangesIter);
        this.port = port;
    }

    protected boolean filter(boolean local, InetSocketAddress sock) {
        return !(local && sock.getPort() == port);
    }
}
