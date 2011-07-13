package org.mca.qmass.test.grid;

import com.hazelcast.core.Hazelcast;
import org.mca.qmass.grid.GridData;
import org.mca.qmass.grid.MapGridDataAdapter;
import org.mca.qmass.test.runner.MainArgs;
import org.mca.qmass.test.runner.RunnerTemplate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.Serializable;
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
        final int numOfInstances = MainArgs.getNumberOfInstances(args);
        DistributeAFileAndGetItBackTemplate t = new DistributeAFileAndGetItBackTemplate() {

            @Override
            protected long getWaitTimeForSetup() {
                return 15000;
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
            protected RunnerTemplate getRunnerTemplate() {
                return new RunnerTemplate(numOfInstances, getOutputDir()) {

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
