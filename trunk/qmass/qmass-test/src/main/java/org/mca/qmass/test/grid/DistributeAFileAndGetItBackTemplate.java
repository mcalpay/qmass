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
package org.mca.qmass.test.grid;

import org.mca.qmass.grid.node.GridData;
import org.mca.qmass.test.runner.AbstractProcessRunner;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.concurrent.CountDownLatch;

/**
 * User: malpay
 * Date: 13.Tem.2011
 * Time: 09:20:12
 */
public abstract class DistributeAFileAndGetItBackTemplate {

    private AbstractProcessRunner rt;

    public void run() throws Exception {
        System.err.println("Starting...");
        long setUpStartTime = System.currentTimeMillis();
        rt = getRunnerTemplate();
        GridData grid = getGridData();
        rt.initProcesses();
        rt.start();
        waitUntilGridIsReady();
        // wait till the cluster is up
        long setUpEndTime = System.currentTimeMillis();
        long startTime = System.currentTimeMillis();
        BufferedInputStream is = getInputFile();
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
        CountDownLatch endGate = new CountDownLatch(getNumOfGridToFileWriterThreads());
        int i = 0;
        while (i < getNumOfGridToFileWriterThreads()) {
            createGridToFileWriter(i, totalChunks, startGate, endGate).start();
            i++;
        }

        startGate.countDown();
        endGate.await();

        long endTime = System.currentTimeMillis();
        System.err.println("Setup time : " + (setUpEndTime - setUpStartTime));
        System.err.println("Spent on get/put : " + (endTime - startTime) +
                ", put : " + (putEndTime - startTime) +
                ", get : " + (endTime - putEndTime));
    }

    protected abstract BufferedInputStream getInputFile();

    public void end() {
        rt.end();
        endGrid();
    }

    protected abstract void endGrid();

    protected abstract void waitUntilGridIsReady();

    protected Thread createGridToFileWriter(int i, int totalChunks, CountDownLatch startGate, CountDownLatch endGate) {
        return new GridToFileWriter(i, getGridData(), totalChunks, getOutputDir(), startGate, endGate);
    }

    protected abstract String getOutputDir();

    protected abstract AbstractProcessRunner getRunnerTemplate();

    protected abstract GridData getGridData();

    protected abstract int getChunkLength();

    protected abstract int getNumOfGridToFileWriterThreads();

    protected abstract Integer getNumOfGridInstances();
}
