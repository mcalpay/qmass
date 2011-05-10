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
        try {
            return InetAddress.getLocalHost().equals(this.hostname) || this.hostname.isLoopbackAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public void init() {
        this.portIndex = portScanStart;
    }
}