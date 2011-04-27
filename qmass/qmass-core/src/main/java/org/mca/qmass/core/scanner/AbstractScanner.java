package org.mca.qmass.core.scanner;

import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketRange;

import java.net.InetSocketAddress;
import java.util.Iterator;

/**
 * User: malpay
 * Date: 26.Nis.2011
 * Time: 14:29:34
 */
public abstract class AbstractScanner implements Scanner {
    private Iterator<SocketRange> rangesIter;

    private SocketRange sr;

    public AbstractScanner(Iterator<SocketRange> rangesIter) {
        this.rangesIter = rangesIter;
        if (rangesIter.hasNext()) {
            this.sr = rangesIter.next();
            this.sr.init();
        }
    }

    @Override
    public final InetSocketAddress scan() {
        if (sr != null) {
            InetSocketAddress sock;
            do {
                sock = sr.nextSocket();
                if (sock != null && filter(sr.isLocal(), sock)) {
                    return sock;
                } else if (sock == null) {
                    if (rangesIter.hasNext()) {
                        this.sr = rangesIter.next();
                    } else {
                        this.sr = null;
                    }
                }
            } while (this.sr != null);
        }
        return null;
    }

    protected abstract boolean filter(boolean local, InetSocketAddress sock);
}
