package org.mca.qmass.core.scanner;

import org.mca.qmass.core.scanner.SocketRange;

import java.net.InetSocketAddress;
import java.util.Iterator;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 15:33:43
 */
public class LocalScanner extends AbstractScanner {

    public LocalScanner(Iterator<SocketRange> rangesIter) {
        super(rangesIter);
    }

    @Override
    protected boolean filter(boolean local, InetSocketAddress sock) {
        return local;
    }
}
