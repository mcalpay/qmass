package org.mca.qmass.test.grid;

import org.mca.qmass.grid.QMassGrid;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 15:23:12
 */
public class DistFileReaderHz extends Thread {

    private int id;

    private Map grid;

    private int totalChunks;

    private ThreadsWatcher watcher;

    public DistFileReaderHz(int id, Map grid, int totalChunks, ThreadsWatcher watcher) {
        this.id = id;
        this.grid = grid;
        this.totalChunks = totalChunks;
        this.watcher = watcher;
    }

    @Override
    public void run() {
        watcher.started();
        try {
            Thread.sleep(100);
            BufferedOutputStream os = new BufferedOutputStream(
                    new FileOutputStream("f:/dists/" + id + ".jpg"));
            int i = 1;
            while (i <= totalChunks) {
                byte chunk[] = (byte[]) grid.get(i);
                os.write(chunk);
                i++;
            }
            os.close();
        } catch (Exception e) {
            System.out.println(id + " throws error.");
        }
        watcher.done();
    }

}