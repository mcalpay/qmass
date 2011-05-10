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
package org.mca.qmass.core;

import org.junit.Test;
import org.mca.qmass.core.scanner.Scanner;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.net.InetSocketAddress;

import static junit.framework.Assert.*;

/**
 * User: malpay
 * Date: 26.Nis.2011
 * Time: 12:00:44
 */
public class SocketScannerManagerTests {

    @Test
    public void scanLocalPortNo1() throws Exception {
        String host = "localhost";
        int port = 1;
        SocketScannerManager ssm = new SocketScannerManager(host, port, port);
        Scanner scanner = ssm.scanLocalSocket();
        assertEquals(new InetSocketAddress(host,port), scanner.scan());
        assertNull(scanner.scan());
    }
    
    @Test
    public void scanLocalPortNo1To2() throws Exception {
        String host = "localhost";
        int portStart = 1;
        int portEnd = 2;
        SocketScannerManager ssm = new SocketScannerManager(host, portStart, portEnd);
        Scanner scanner = ssm.scanLocalSocket();
        assertEquals(new InetSocketAddress(host,portStart), scanner.scan());
        assertEquals(new InetSocketAddress(host,portEnd), scanner.scan());
        assertNull(scanner.scan());
    }

    @Test
    public void scanTwoDifferentHostsWith2Ports() throws Exception {
        String host1 = "localhost";
        String host2 = "10.10.10.10";
        int portStart1 = 1;
        int portEnd1 =  2;
        int portStart2 = 5;
        int portEnd2 =  6;
        SocketScannerManager ssm = new SocketScannerManager(host1, portStart1, portEnd1);
        ssm.addSockets(host2,portStart2,portEnd2);
        Scanner scanner = ssm.scanSocket();
        assertEquals(new InetSocketAddress(host1,portStart1), scanner.scan());
        assertEquals(new InetSocketAddress(host1,portEnd1), scanner.scan());
        assertEquals(new InetSocketAddress(host2,portStart2), scanner.scan());
        assertEquals(new InetSocketAddress(host2,portEnd2), scanner.scan());
        assertNull(scanner.scan());
    }

    @Test
    public void scanLocalWithTwoDifferentHostsWith2Ports() throws Exception {
        String host1 = "localhost";
        String host2 = "10.10.10.10";
        int portStart1 = 1;
        int portEnd1 =  2;
        int portStart2 = 5;
        int portEnd2 =  6;
        SocketScannerManager ssm = new SocketScannerManager(host1, portStart1, portEnd1);
        ssm.addSockets(host2,portStart2,portEnd2);
        Scanner scanner = ssm.scanLocalSocket();
        assertEquals(new InetSocketAddress(host1,portStart1), scanner.scan());
        assertEquals(new InetSocketAddress(host1,portEnd1), scanner.scan());
        assertNull(scanner.scan());
    }

    @Test
    public void scanPortNo1To2Except1() throws Exception {
        String host = "localhost";
        int portStart = 1;
        int portEnd = 2;
        SocketScannerManager ssm = new SocketScannerManager(host, portStart, portEnd);
        Scanner scanner = ssm.scanSocketExceptLocalPort(1);
        assertEquals(new InetSocketAddress(host,portEnd), scanner.scan());
        assertNull(scanner.scan());
    }
    
    @Test
    public void scanPortNo1To2Except2() throws Exception {
        String host = "localhost";
        int portStart = 1;
        int portEnd = 2;
        SocketScannerManager ssm = new SocketScannerManager(host, portStart, portEnd);
        Scanner scanner = ssm.scanSocketExceptLocalPort(2);
        assertEquals(new InetSocketAddress(host,portStart), scanner.scan());
        assertNull(scanner.scan());
    }

}
