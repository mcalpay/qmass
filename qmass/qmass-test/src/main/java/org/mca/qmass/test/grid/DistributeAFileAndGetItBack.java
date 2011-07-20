package org.mca.qmass.test.grid;

import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.node.GridData;
import org.mca.qmass.grid.QMassGrid;
import org.mca.qmass.test.runner.MainArgs;
import org.mca.qmass.test.runner.ProcessRunnerTemplate;

import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * User: malpay
 * Date: 08.Tem.2011
 * Time: 15:59:07
 */
public class DistributeAFileAndGetItBack {

    // @TODO error @ private static final int CHUNKLENGTH = 2048;
    private static final int CHUNKLENGTH = 1024;

    private static final int NUMOFREADERS = 8;

    public static void main(String... args) throws Exception {
        System.setOut(new PrintStream(new FileOutputStream("f:/dists/main.in")));
        final int numOfInstances = MainArgs.getNumberOfInstances(args);
        final String LIBDIR = "F:/qmass/dependencies/";
        final String ARTIFACTSDIR = LIBDIR;
        DistributeAFileAndGetItBackTemplate t = new DistributeAFileAndGetItBackTemplate() {

            private QMassGrid grid = new QMassGrid("m", QMass.getQMass());

            @Override
            protected void endGrid() {
                QMass.getQMass().end();
            }

            @Override
            protected void waitUntilGridIsReady() {
                while (QMass.getQMass().getClusterManager().getCluster().length
                        < getNumOfGridInstances()) {
                }
                
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected String getInputFilePath() {
                return "F:/kbs.jpg";
            }

            @Override
            protected String getOutputDir() {
                return "F:/dists/";
            }

            @Override
            protected ProcessRunnerTemplate getRunnerTemplate() {
                return new ProcessRunnerTemplate(getNumOfGridInstances(), getOutputDir()) {

                    @Override
                    protected String getRunString() {
                        String elConsole = "java -cp " +
                                ARTIFACTSDIR + "qmass.jar;" +
                                ARTIFACTSDIR + "qmass_test.jar;" +
                                LIBDIR + "commons-logging-1.1.1.jar;" +
                                LIBDIR + "log4j-1.2.16.jar;" +
                                LIBDIR + "el-api-2.2.jar;" +
                                LIBDIR + "el-impl-2.2.jar" +
                                " " +
                                "org.mca.qmass.console.ELConsoleMain";
                        return elConsole;
                    }

                };
            }

            @Override
            protected GridData getGridData() {
                return grid;
            }

            @Override
            protected int getChunkLength() {
                return CHUNKLENGTH;
            }

            @Override
            protected int getNumOfGridToFileWriterThreads() {
                return NUMOFREADERS;
            }

            @Override
            protected Integer getNumOfGridInstances() {
                return numOfInstances;
            }
        };

        t.run();
    }

}
