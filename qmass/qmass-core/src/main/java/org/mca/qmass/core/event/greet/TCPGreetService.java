package org.mca.qmass.core.event.greet;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.cluster.service.EventService;
import org.mca.qmass.core.scanner.Scanner;

import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 14.Tem.2011
 * Time: 16:57:48
 */
public class TCPGreetService extends DefaultGreetService {

    public TCPGreetService(QMass qmass, EventService es, Scanner scanner) {
        super(qmass, es, scanner);
    }

}
