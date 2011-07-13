package org.mca.qmass.test.grid;

import com.hazelcast.core.Hazelcast;
import org.mca.qmass.grid.GridData;
import org.mca.qmass.test.runner.ProcessRunnerTemplate;
import sun.net.httpserver.HttpsServerImpl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

/**
 * User: malpay
 * Date: 13.Tem.2011
 * Time: 09:20:12
 */
public abstract class DistributeAFileAndGetItBackTemplate {

    public void run() throws Exception {
        System.setOut(new PrintStream(new FileOutputStream(getOutputDir() + "main.in")));
        System.err.println("Starting...");
        ProcessRunnerTemplate rt = getRunnerTemplate();
        GridData grid = getGridData();
        rt.start();
        waitUntilGridIsReady();
        // wait till the cluster is up
        System.err.println("Putting...");
        long startTime = System.currentTimeMillis();
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(getInputFilePath()));
        //BufferedInputStream is = new BufferedInputStream(new FileInputStream("f:/file.txt"));
        int totalChunks = 0;
        //int len = 3;
        while (is.available() != 0) {
            byte chunk[] = new byte[getChunkLength()];
            int red = is.read(chunk, 0, getChunkLength());
            if (red < getChunkLength()) {
                byte correctChunk[] = new byte[red];
                System.arraycopy(chunk, 0, correctChunk, 0, red);
                chunk = correctChunk;
            }
            totalChunks++;
            if (!grid.put(totalChunks, chunk)) {
                System.err.println("put failed for key " + totalChunks);
            }
        }

        is.close();

        long putEndTime = System.currentTimeMillis();
        System.err.println("Put ended. Total # of chunks : " + totalChunks);

        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(getNumOfReaderThreads());
        int i = 0;
        while (i < getNumOfReaderThreads()) {
            getReaderThread(i, totalChunks, startGate, endGate).start();
            i++;
        }

        startGate.countDown();
        endGate.await();

        long endTime = System.currentTimeMillis();
        System.err.println("Spent on get/put : " + (endTime - startTime) +
                ", put : " + (putEndTime - startTime) +
                ", get : " + (endTime - putEndTime));
        rt.end();
        endGrid();
    }

    protected abstract void endGrid();

    protected abstract void waitUntilGridIsReady();

    protected Thread getReaderThread(int i, int totalChunks, CountDownLatch startGate, CountDownLatch endGate) {
        return new DistFileReader(i, getGridData(), totalChunks, getOutputDir(), startGate, endGate);
    }

    protected abstract String getInputFilePath();

    protected abstract String getOutputDir();

    protected abstract ProcessRunnerTemplate getRunnerTemplate();

    protected abstract GridData getGridData();

    protected abstract int getChunkLength();

    protected abstract int getNumOfReaderThreads();

    protected abstract Integer getNumOfGridInstances();
}
