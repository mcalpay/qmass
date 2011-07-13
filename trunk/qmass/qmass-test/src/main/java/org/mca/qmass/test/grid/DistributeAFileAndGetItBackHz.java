package org.mca.qmass.test.grid;

import com.hazelcast.core.Hazelcast;
import org.mca.qmass.grid.GridData;
import org.mca.qmass.grid.MapGridDataAdapter;
import org.mca.qmass.test.runner.ProcessRunnerTemplate;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 13:37:16
 */
public class DistributeAFileAndGetItBackHz {

    private static final int CHUNKLENGTH = 2048;

    private static final int NUMOFREADERS = 8;

    public static void main(String... args) throws Exception {
        final int numOfInstances = 2;//MainArgs.getNumberOfInstances(args);
        final String LIBDIR = "F:/qmass/dependencies/";
        DistributeAFileAndGetItBackTemplate t = new DistributeAFileAndGetItBackTemplate() {

            @Override
            protected void endGrid() {
                Hazelcast.shutdownAll();
            }

            @Override
            protected void waitUntilGridIsReady() {
                while (Hazelcast.getCluster().getMembers().size() + 1 < getNumOfGridInstances()) {
                }
                System.err.println("Cluster is ready.");
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
                return new ProcessRunnerTemplate(numOfInstances, getOutputDir()) {

                    @Override
                    protected String getRunString() {
                        String elConsole = "java -cp " +
                                LIBDIR + "qmass.jar;" +
                                LIBDIR + "qmass_test.jar;" +
                                LIBDIR + "commons-logging-1.1.1.jar;" +
                                LIBDIR + "log4j-1.2.16.jar;" +
                                LIBDIR + "hazelcast-1.9.2.jar" +
                                " " +
                                "org.mca.qmass.test.grid.DistributeAFileAndGetItBackHzMain";
                        return elConsole;
                    }
                };
            }

            @Override
            protected GridData getGridData() {
                return new MapGridDataAdapter(Hazelcast.getMap("m"));
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
