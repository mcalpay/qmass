/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mca.qmass.core.scanner;

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
