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
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 15:04:31
 */
public class GridToFileWriter extends Thread {

    protected final YALog logger = YALogFactory.getLog(getClass());

    private Serializable id;

    private GridData grid;

    private int totalChunks;

    private String outputDir;

    private CountDownLatch startGate;

    private CountDownLatch endGate;

    public GridToFileWriter(Serializable id, GridData grid, int totalChunks, String outputDir,
                            CountDownLatch startGate, CountDownLatch endGate) {
        super("GridToFileWriter@" + id.toString());
        this.id = id;
        this.grid = grid;
        this.totalChunks = totalChunks;
        this.outputDir = outputDir;
        this.startGate = startGate;
        this.endGate = endGate;
    }


    @Override
    public void run() {
        int i = 1;
        try {
            startGate.await();
            BufferedOutputStream os = new BufferedOutputStream(
                    new FileOutputStream(outputDir + "/" + id + ".jpg"));

            while (i <= totalChunks) {
                byte chunk[] = (byte[]) grid.get(i);
                os.write(chunk);
                i++;
            }
            os.close();

        } catch (Exception e) {
            logger.error(id + " throws error: " + e.getMessage() + ", error at " + i, e);
            System.err.println(id + " throws error: " + e.getMessage() + ", error at " + i);
        } finally {
            this.endGate.countDown();
        }

    }

}
