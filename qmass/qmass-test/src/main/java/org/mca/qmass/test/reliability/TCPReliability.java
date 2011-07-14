package org.mca.qmass.test.reliability;

import org.mca.qmass.grid.id.DefaultIdGenerator;
import org.mca.qmass.grid.id.IdGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: malpay
 * Date: 14.Tem.2011
 * Time: 11:33:38
 */
public class TCPReliability  implements Serializable {

    private static final InetSocketAddress LISTENINGSOCKET = new InetSocketAddress("localhost", 9999);

    private static final InetSocketAddress TALKERSOCKET = new InetSocketAddress("localhost", 8888);

    private static final Integer numOfTalkers = 8;

    private Integer messageCount;

    private Integer successFullMessageCount;

    public static void main(String... args) throws Exception {
        Integer totalMessageCount = 0;
        Integer totalSuccesfullMessageCount = 0;
        for (int i = 0; i < 5; i++) {
            TCPReliability reliability = new TCPReliability();
            reliability.run();
            totalMessageCount += reliability.messageCount;
            totalSuccesfullMessageCount += reliability.successFullMessageCount;
            printStats(i, reliability.successFullMessageCount, reliability.messageCount);
        }

        printStats("total", totalSuccesfullMessageCount, totalMessageCount);
    }

    private static void printStats(Serializable id, int recieved, int send) {
        System.out.println("run " + id + "\n recieved : " + recieved + ", send : " + send +
                "\n success : " + ((double) recieved / (double) send));
    }

    private void run() throws Exception {
        DatagramChannel talkerChannel = DatagramChannel.open();
        talkerChannel.configureBlocking(false);
        talkerChannel.socket().bind(TALKERSOCKET);
        IdGenerator idGenerator = new DefaultIdGenerator();
        Listener l = new Listener();
        l.start();
        List<Talker> talkers = new ArrayList<Talker>();
        for (int i = 0; i < numOfTalkers; i++) {
            Talker t = new Talker(talkerChannel, LISTENINGSOCKET, idGenerator);
            t.start();
            talkers.add(t);
        }

        Thread.sleep(10000);
        for (Talker t : talkers) {
            t.runningFalse();
        }
        Thread.sleep(10000);
        l.runningFalse();

        messageCount = idGenerator.nextId() - 1;
        successFullMessageCount = l.receivedMessageCount;
        talkerChannel.close();
    }

    public class Talker extends Thread {

        private volatile boolean running = true;

        private DatagramChannel channel;

        private SocketAddress to;

        private Random random;

        private IdGenerator generator;

        public Talker(DatagramChannel channel, SocketAddress to, IdGenerator generator) {
            this.channel = channel;
            this.to = to;
            this.random = new Random(System.currentTimeMillis());
            this.generator = generator;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(100);
                while (running) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    new ObjectOutputStream(bos).writeObject(
                            new Message(generator.nextId(), message));
                    byte[] data = bos.toByteArray();
                    ByteBuffer buffer = ByteBuffer.allocate(data.length);
                    buffer.put(data);
                    buffer.flip();
                    int sent = channel.send(buffer, to);
                    Thread.sleep((int) (25 + random.nextDouble() * 75));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void runningFalse() {
            running = false;
        }
    }

    public class Listener extends Thread {

        private volatile boolean running = true;

        @Override
        public void run() {
            DatagramChannel channel = null;
            try {
                channel = DatagramChannel.open();
                channel.configureBlocking(false);
                channel.socket().bind(LISTENINGSOCKET);
                while (running) {
                    ByteBuffer buffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
                    while (channel.receive(buffer) != null) {
                        buffer.flip();
                        byte[] buf = new byte[buffer.remaining()];
                        buffer.get(buf);
                        Message msg = (Message) new ObjectInputStream(new ByteArrayInputStream(buf)).readObject();
                        listen(msg);
                        buffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (channel != null) {
                    channel.socket().close();
                }
            }
        }

        private Integer receivedMessageCount = 0;

        private void listen(Message msg) {
            receivedMessageCount++;
        }

        public void printStatistics() {
            System.out.println("receivedMessageCount : " + receivedMessageCount);
        }

        public void runningFalse() {
            running = false;
        }
    }

    public class Message implements Serializable {

        private Integer messageNo;

        private String message;

        public Message(Integer messageNo, String message) {
            this.messageNo = messageNo;
            this.message = message;
        }

        public Integer getMessageNo() {
            return messageNo;
        }

        public String getMessage() {
            return message;
        }
    }

    private static final String message = "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca\n" +
            "mcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamcamca";
}
