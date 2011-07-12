package org.mca.qmass.test.grid;

import org.mca.qmass.grid.QMassGrid;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 15:04:31
 */
public class DistFileReader extends Thread {

    private int id;

    private QMassGrid grid;

    private int totalChunks;

    private ThreadsWatcher watcher;

    public DistFileReader(int id, QMassGrid grid, int totalChunks, ThreadsWatcher watcher) {
        this.id = id;
        this.grid = grid;
        this.totalChunks = totalChunks;
        this.watcher = watcher;
    }


    @Override
    public void run() {
        watcher.started();
        int i = 1;
        try {
            Thread.sleep(100);
            BufferedOutputStream os = new BufferedOutputStream(
                    new FileOutputStream("f:/dists/" + id + ".jpg"));

            while (i <= totalChunks) {
                byte chunk[] = (byte[]) grid.get(i);
                os.write(chunk);
                i++;
            }
            os.close();
        } catch (Exception e) {
            System.err.println(id + " throws error: " + e.getMessage() + ", error at " + i);
        }
        watcher.done();
    }

}
