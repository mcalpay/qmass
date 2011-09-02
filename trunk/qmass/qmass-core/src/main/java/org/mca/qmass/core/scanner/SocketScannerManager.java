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

import org.mca.qmass.core.utils.IPUtils;

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
        String[] first = cluster.split("/");
        int i = 0;
        while (i < first.length) {
            String[] second = first[i].split(",");
            addSockets(second[0], Integer.valueOf(second[1]), Integer.valueOf(second[2]));
            i++;
        }
    }

    public SocketScannerManager addSockets(String hostname, Integer portScanStart, Integer portScanEnd) {
        if ("localhost".equals(hostname)) {
            hostname = IPUtils.getLocalIpAsString();
        }
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
        return new ExceptLocalPortScanner(ranges.iterator(), port);
    }
}
