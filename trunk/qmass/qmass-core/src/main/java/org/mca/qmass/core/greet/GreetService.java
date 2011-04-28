package org.mca.qmass.core.greet;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.scanner.Scanner;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 10:16:15
 */
public interface GreetService extends Service {

    GreetService greet();

    GreetService greetIfHeDoesntKnowMe(InetSocketAddress who, List<InetSocketAddress> knowsWho);
}
