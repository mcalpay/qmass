package org.mca.qmass.test;

import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.QMassGrid;
import org.mca.qmass.runner.MainArgs;
import org.mca.qmass.runner.RunnerTemplate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

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
        RunnerTemplate rt = new RunnerTemplate(numOfInstances) {

            @Override
            protected boolean isTrackPrints() {
                return true;
            }

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
        QMassGrid grid = new QMassGrid("m", qmass);
        rt.start();
        Thread.sleep(5000);
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
