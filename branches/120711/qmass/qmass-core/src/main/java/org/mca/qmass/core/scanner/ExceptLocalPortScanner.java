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
 * Date: 25.Nis.2011
 * Time: 17:16:58
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
