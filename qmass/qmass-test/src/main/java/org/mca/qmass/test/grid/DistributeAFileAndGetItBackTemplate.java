package org.mca.qmass.test.grid;

import org.mca.qmass.grid.GridData;
import org.mca.qmass.test.runner.RunnerTemplate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * User: malpay
 * Date: 13.Tem.2011
 * Time: 09:20:12
 */
public abstract class DistributeAFileAndGetItBackTemplate {

    public void run() throws Exception {
        System.setOut(new PrintStream(new FileOutputStream(getOutputDir() + "\\main.in")));
        System.err.println("Starting...");
        RunnerTemplate rt = getRunnerTemplate();
        GridData grid = getGridData();
        rt.start();
        Thread.sleep(getWaitTimeForSetup());

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
        ThreadsWatcher w = new ThreadsWatcher();

        int i = 0;
        while (i < getNumOfReaderThreads()) {
            getReaderThread(i, totalChunks, w).start();
            i++;
        }

        while (true) {
            if (w.isAllDone()) {
                break;
            }
        }

        long endTime = System.currentTimeMillis();
        System.err.println("Spent on get/put : " + (endTime - startTime) +
                ", put : " + (putEndTime - startTime) +
                ", get : " + (endTime - putEndTime));
    }

    protected Thread getReaderThread(int i, int totalChunks, ThreadsWatcher w) {
        return new DistFileReader(i, getGridData(), totalChunks, w, getOutputDir());
    }

    protected abstract long getWaitTimeForSetup();

    protected abstract String getInputFilePath();

    protected abstract String getOutputDir();

    protected abstract RunnerTemplate getRunnerTemplate();

    protected abstract GridData getGridData();

    protected abstract int getChunkLength();

    protected abstract int getNumOfReaderThreads();

    protected abstract Integer getNumOfGridInstances();
}
