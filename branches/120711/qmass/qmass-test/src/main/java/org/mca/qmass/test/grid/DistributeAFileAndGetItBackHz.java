package org.mca.qmass.test.grid;

import com.hazelcast.core.Hazelcast;
import org.mca.qmass.grid.GridData;
import org.mca.qmass.grid.MapGridDataAdapter;
import org.mca.qmass.test.runner.MainArgs;
import org.mca.qmass.test.runner.RunnerTemplate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Map;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 13:37:16
 */
public class DistributeAFileAndGetItBackHz {

    private static final int CHUNKLENGTH = 2048;

    private static final int NUMOFREADERS = 8;
    
    public static void main(String... args) throws Exception {
        int numOfInstances = MainArgs.getNumberOfInstances(args);
        RunnerTemplate rt = new RunnerTemplate(numOfInstances) {

            @Override
            protected String getRunString() {
                String elConsole = "java -cp " +
                        "qmass.jar;" +
                        "dependencies/commons-logging-1.1.1.jar;" +
                        "dependencies/log4j-1.2.16.jar;" +
                        "dependencies/hazelcast-1.9.2.jar" +
                        " " +
                        "org.mca.qmass.test.grid.DistributeAFileAndGetItBackHzMain";
                return elConsole;
            }

        };

        rt.start();
        GridData grid = new MapGridDataAdapter(Hazelcast.getMap("m"));

        Thread.sleep(15000);
        // wait till the cluster is up
        System.out.println("Start...");
        long startTime = System.currentTimeMillis();
        BufferedInputStream is = new BufferedInputStream(new FileInputStream("f:/kbs.JPG"));
        //BufferedInputStream is = new BufferedInputStream(new FileInputStream("f:/file.txt"));
        int totalChunks = 0;
        //int len = 3;

        while (is.available() != 0) {
            byte chunk[] = new byte[CHUNKLENGTH];
            int red = is.read(chunk, 0, CHUNKLENGTH);
            if (red < CHUNKLENGTH) {
                byte correctChunk[] = new byte[red];
                System.arraycopy(chunk, 0, correctChunk, 0, red);
                chunk = correctChunk;
            }
            totalChunks++;

            //System.out.println("READ> " + totalChunks + ", " + new String(chunk));
            grid.put(totalChunks, chunk);
        }

        is.close();
        
        long putEndTime = System.currentTimeMillis();

        System.err.println("Total # of chunks : " + totalChunks);

        ThreadsWatcher w = new ThreadsWatcher();

        int i = 0;
        while (i < NUMOFREADERS) {
            new DistFileReader(i, grid, totalChunks, w).start();
            i++;
        }

        while (true) {
            if (w.isAllDone()) {
                break;
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Spent on get/put : " + (endTime - startTime) +
                ", put : " + (putEndTime - startTime) +
                ", get : " + (endTime - putEndTime));
    }

}
