package org.mca.qmass.core.event.greet;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.greet.GreetEvent;
import org.mca.qmass.core.scanner.Scanner;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * User: malpay
 * Date: 28.Nis.2011
 * Time: 10:18:24
 */
public class DefaultGreetService implements GreetService {

    private Serializable id;

    private QMass qmass;

    private InetSocketAddress listeningAt;

    private Scanner scanner;

    public Serializable getId() {
        return id;
    }

    public DefaultGreetService(QMass qmass, InetSocketAddress listeningAt, Scanner scanner) {
        this.id = qmass.getId() + "greet";
        this.qmass = qmass;
        this.listeningAt = listeningAt;
        this.scanner = scanner;
        this.qmass.registerService(this);
    }


    @Override
    public GreetService greet() {
        qmass.sendEvent(scanner,
                new GreetEvent(qmass, this, listeningAt));
        return this;
    }

    @Override
    public GreetService greetIfHeDoesntKnowMe(InetSocketAddress who, InetSocketAddress[] knowsWho) {
        if (!Arrays.asList(knowsWho).contains(listeningAt)) {
            qmass.sendEvent(who, new GreetEvent(qmass, this, listeningAt));
        }
        return this;
    }
}