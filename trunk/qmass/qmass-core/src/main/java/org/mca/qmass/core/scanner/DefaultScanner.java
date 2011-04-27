package org.mca.qmass.core.scanner;

import org.mca.qmass.core.scanner.SocketRange;

import java.net.InetSocketAddress;
import java.util.Iterator;

/**
 * User: malpay
 * Date: 25.Nis.2011
 * Time: 15:29:51
 */
public class DefaultScanner extends AbstractScanner {

    public DefaultScanner(Iterator<SocketRange> rangesIter) {
        super(rangesIter);
    }

    protected boolean filter(boolean local, InetSocketAddress sock) {
        return true;
    }
}
