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
package org.mca.qmass.test.reliability;

import org.mca.qmass.core.id.DefaultIdGenerator;
import org.mca.qmass.core.id.IdGenerator;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: malpay
 * Date: 14.Tem.2011
 * Time: 11:33:38
 */
public class TCPReliability implements Serializable {

    private static final InetSocketAddress LISTENINGSOCKET = new InetSocketAddress("localhost", 9999);

    private static final InetSocketAddress TALKERSOCKET = new InetSocketAddress("localhost", 8888);

    private static final int TESTNUMBER = 1;

    private static final Integer numOfTalkers = 16;

    private Integer messageCount;

    private Integer successFullMessageCount;

    public static void main(String... args) throws Exception {
        Integer totalMessageCount = 0;
        Integer totalSuccesfullMessageCount = 0;
        for (int i = 0; i < TESTNUMBER; i++) {
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
        Listener l = new Listener();
        l.start();

        SocketChannel talkerChannel = SocketChannel.open();
        talkerChannel.configureBlocking(false);
        talkerChannel.socket().bind(TALKERSOCKET);
        talkerChannel.connect(LISTENINGSOCKET);
        while (!talkerChannel.finishConnect()) {
        }

        IdGenerator idGenerator = new DefaultIdGenerator();
        List<Talker> talkers = new ArrayList<Talker>();
        for (int i = 0; i < numOfTalkers; i++) {
            Talker t = new Talker(talkerChannel, idGenerator);
            t.start();
            talkers.add(t);
        }

        Thread.sleep(10000);
        for (Talker t : talkers) {
            t.runningFalse();
        }
        Thread.sleep(10000);
        l.runningFalse();

        messageCount = idGenerator.nextId() - TESTNUMBER;
        successFullMessageCount = l.receivedMessageCount;
        talkerChannel.close();
    }

    public class Talker extends Thread {

        private volatile boolean running = true;

        private SocketChannel channel;

        private Random random;

        private IdGenerator generator;

        public Talker(SocketChannel channel, IdGenerator generator) {
            this.channel = channel;
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
                    channel.write(buffer);
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
            ServerSocketChannel channel;
            try {
                channel = ServerSocketChannel.open();
                channel.configureBlocking(false);
                channel.socket().bind(LISTENINGSOCKET);
                SocketChannel sc = null;
                while (sc == null) {
                    sc = channel.accept();
                }
                     
                while (running) {
                    Message msg = (Message) new ObjectInputStream(sc.socket().getInputStream()).readObject();
                    listen(msg);
                }

                sc.close();
            } catch (Exception e) {
                e.printStackTrace();
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
