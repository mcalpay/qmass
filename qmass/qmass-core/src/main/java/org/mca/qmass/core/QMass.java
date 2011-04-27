package org.mca.qmass.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.ir.IR;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;
import org.mca.qmass.core.event.GreetEvent;
import org.mca.qmass.core.scanner.SocketScannerManager;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;

/**
 * User: malpay
 * listeningAt, cluster and constructor is assigned package/public level visibility to enable writing test cases.
 * Date: 25.Nis.2011
 * Time: 14:13:09
 */
public class QMass {

    private static final Log logger = LogFactory.getLog(QMass.class);

    private static final Map<Serializable, QMass> masses = new HashMap<Serializable, QMass>();

    InetSocketAddress listeningAt;

    Set<InetSocketAddress> cluster;

    private Serializable id;

    private DatagramChannel channel;

    private SocketScannerManager scannerManager;

    private Timer timer;
    
    private Map<Serializable,Service> services = new HashMap();

    public static QMass getQMass() {
        QMass mass = masses.get(getIR().DEFAULT);
        if (mass == null) {
            mass = new QMass(getIR().DEFAULT);
            masses.put(getIR().DEFAULT, mass);
        }
        return mass;
    }

    public static QMass getQMass(Serializable id) {
        QMass mass = masses.get(id);
        if (mass == null) {
            mass = new QMass(id);
            masses.put(id, mass);
        }
        return mass;
    }

    public static QMassIR getIR() {
        return (QMassIR) IR.getIR(QMassIR.class);
    }

    public QMass(Serializable id) {
        logger.info("QMass is starting, id : " + id);
        this.id = id;
        this.scannerManager = new SocketScannerManager(getIR().getCluster());
        initChannel();
        startListening();
        this.timer = new Timer();
        this.timer.start();
        this.cluster = new HashSet<InetSocketAddress>();
        sendEvent(this.scannerManager.scanSocketExceptLocalPort(listeningAt.getPort()),
                new GreetEvent(id, listeningAt, cluster));
    }

    public Serializable getId() {
        return id;
    }

    public QMass sendEvent(Event event) {
        byte[] bytes = event.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        for (InetSocketAddress to : cluster) {
            try {
                buffer.flip();
                int sent = channel.send(buffer, to);
                if (sent != bytes.length) {
                    logger.warn("sent " + sent + " bytes of " + bytes.length + " to " + to);
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return this;
    }

    private QMass sendEvent(org.mca.qmass.core.scanner.Scanner scanner, Event event) {
        byte[] bytes = event.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        InetSocketAddress to = scanner.scan();
        while (to != null) {
            try {
                buffer.flip();
                int sent = channel.send(buffer, to);
                if (sent != bytes.length) {
                    logger.warn("sent " + sent + " bytes of " + bytes.length + " to " + to);
                }
            } catch (IOException e) {
                logger.error(e);
            }

            to = scanner.scan();
        }
        return this;
    }

    private QMass sendEvent(InetSocketAddress to, GreetEvent event) {
        byte[] bytes = event.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        try {
            buffer.flip();
            int sent = channel.send(buffer, to);
            if (sent != bytes.length) {
                logger.warn("sent " + sent + " bytes of " + bytes.length + " to " + to);
            }
        } catch (IOException e) {
            logger.error(e);
        }
        return this;
    }

    private QMass handleEvent() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getIR().getCapacity());
            while (this.channel.receive(buffer) != null) {
                buffer.flip();
                StringBuilder evid = new StringBuilder();
                StringBuilder evhandler = new StringBuilder();
                byte b = buffer.get();
                while (buffer.hasRemaining() && b != getIR().SEPARTOR) {
                    evid.append(Character.valueOf((char) b));
                    b = buffer.get();
                }

                if (buffer.hasRemaining()) {
                    b = buffer.get();
                }

                while (buffer.hasRemaining() && b != getIR().SEPARTOR) {
                    evhandler.append((char) b);
                    b = buffer.get();
                }

                logger.debug(listeningAt + ", " + id + " received; " + evid + ", " + evhandler);
                if (evid.toString().equals(id.toString())) {
                    try {
                        EventHandler handler = (EventHandler) Class.forName(evhandler.toString()).newInstance();
                        handler.handleEvent(this, buffer);
                    } catch (Exception e) {
                        logger.error("error trying to handle event",e);
                    }
                }

                buffer.clear();
            }
        } catch (IOException e) {
            logger.error(e);
        }
        return this;
    }

    public QMass end() {
        this.timer.end();
        this.channel.socket().close();
        return this;
    }

    private void startListening() {
        org.mca.qmass.core.scanner.Scanner scanner = scannerManager.scanLocalSocket();
        InetSocketAddress socket = scanner.scan();
        while (socket != null) {
            try {
                this.channel.socket().bind(socket);
                listeningAt = socket;
                break;
            } catch (SocketException e) {
                socket = scanner.scan();
            }
        }

        if (listeningAt == null) {
            throw new RuntimeException("Couldnt find a free port to listen!");
        }
    }

    private void initChannel() {
        try {
            this.channel = DatagramChannel.open();
            this.channel.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public QMass addSocketToCluster(InetSocketAddress who) {
        logger.info(listeningAt + " adding to cluster : " + who);
        cluster.add(who);
        return this;
    }

    public QMass greetIfHeDoesntKnowMe(InetSocketAddress who, List<InetSocketAddress> knowsWho) {
        if (!knowsWho.contains(listeningAt)) {
            logger.info(listeningAt + " greets back : " + who);
            sendEvent(who, new GreetEvent(id, listeningAt, cluster));
        }
        return this;
    }

    public Service getService(Serializable id) {
        return services.get(id);
    }

    public QMass registerService(Service service) {
        services.put(service.getId(),service);
        return this;
    }

    private class Timer extends Thread {
        private boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    handleEvent();
                    Thread.sleep(getIR().getDefaultThreadWait());
                } catch (InterruptedException e) {
                    logger.warn("interrupted.", e);
                }
            }
        }

        private void end() {
            running = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QMass mass = (QMass) o;

        if (!id.equals(mass.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "QMass{" +
                "listeningAt=" + listeningAt +
                ", id=" + id +
                ", cluster=" + cluster +
                '}';
    }
}
