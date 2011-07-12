package org.mca.qmass.test.grid;

import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.GridData;
import org.mca.qmass.test.runner.MainArgs;
import org.mca.qmass.test.runner.RunnerTemplate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 08.Tem.2011
 * Time: 15:59:07
 */
public class DistributeAFileAndGetItBack {

    private static final int CHUNKLENGTH = 2048;

    private static final int NUMOFREADERS = 8;

    public static void main(String... args) throws Exception {
        int numOfInstances = MainArgs.getNumberOfInstances(args);
        System.setOut(new PrintStream(new FileOutputStream("F:\\dists\\main.in")));
        System.err.println("Starting...");
        RunnerTemplate rt = new RunnerTemplate(numOfInstances) {

            @Override
            protected String getRunString() {
                String elConsole = "java -cp " +
                        "qmass.jar;" +
                        "dependencies/commons-logging-1.1.1.jar;" +
                        "dependencies/log4j-1.2.16.jar;" +
                        "dependencies/el-api-2.2.jar;" +
                        "dependencies/el-impl-2.2.jar" +
                        " " +
                        "org.mca.qmass.console.ELConsoleMain";
                return elConsole;
            }

        };

        QMass qmass = QMass.getQMass();
        GridData grid = new GridData() {
            @Override
            public Boolean put(Serializable key, Serializable value) {
                return null;
            }

            @Override
            public Serializable get(Serializable key) {
                return null;
            }

            @Override
            public Serializable remove(Serializable key) {
                return null;
            }
        };

        rt.start();
        Thread.sleep(5000);
        // wait till the cluster is up
        System.err.println("Putting...");
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
            if (!grid.put(totalChunks, chunk)) {
                System.err.println("put failed for key " + totalChunks);
            }
        }

        is.close();

        long putEndTime = System.currentTimeMillis();

        System.err.println("Put ended. Total # of chunks : " + totalChunks);
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
        System.err.println("Spent on get/put : " + (endTime - startTime) +
                ", put : " + (putEndTime - startTime) +
                ", get : " + (endTime - putEndTime));
    }

}
