package org.mca.qmass.test.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.grid.node.GridData;

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

    private final Log logger = LogFactory.getLog(getClass());

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
