package org.mca.qmass.test.grid;

import org.mca.qmass.grid.GridData;
import org.mca.qmass.test.runner.MainArgs;
import org.mca.qmass.test.runner.ProcessRunnerTemplate;

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
        final int numOfInstances = MainArgs.getNumberOfInstances(args);
        DistributeAFileAndGetItBackTemplate t = new DistributeAFileAndGetItBackTemplate() {

            @Override
            protected void endGrid() {
            }

            @Override
            protected void waitUntilGridIsReady() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            }

            @Override
            protected String getInputFilePath() {
                return "f:/kbs.JPG";
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
            }

            @Override
            protected GridData getGridData() {
                return new GridData() {
                    @Override
                    public Boolean put(Serializable key, Serializable value) {
                        return Boolean.FALSE;
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
            }

            @Override
            protected int getChunkLength() {
                return CHUNKLENGTH;
            }

            @Override
            protected int getNumOfReaderThreads() {
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
