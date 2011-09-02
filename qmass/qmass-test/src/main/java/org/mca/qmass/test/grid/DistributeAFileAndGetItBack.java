/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mca.qmass.test.grid;

import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.node.GridData;
import org.mca.qmass.grid.QMassGrid;
import org.mca.qmass.test.runner.MainArgs;
import org.mca.qmass.test.runner.ProcessRunnerTemplate;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * User: malpay
 * Date: 08.Tem.2011
 * Time: 15:59:07
 */
public class DistributeAFileAndGetItBack {

    private static final int CHUNKLENGTH = 128;

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
                System.err.println("waiting for " + getNumOfGridInstances() + " instances to join.");
                int len = QMass.getQMass().getEventService().getCluster().length;
                while (QMass.getQMass().getEventService().getCluster().length
                        < getNumOfGridInstances()) {
                    InetSocketAddress[] cluster = QMass.getQMass().getEventService().getCluster();
                    int curr = cluster.length;
                    if (curr != len) {
                        len = curr;
                        System.err.println("cluster : " + Arrays.asList(cluster));
                    }
                }

                System.err.println("final cluster : " + Arrays.asList(QMass.getQMass().getEventService().getCluster()));
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
                                LIBDIR + "el-impl-2.2.jar;" +
                                LIBDIR + "groovy-1.8.1.jar;" +
                                LIBDIR + "asm-3.2.jar;" +
                                LIBDIR + "asm-analysis-3.2.jar;" +
                                LIBDIR + "asm-commons-3.2.jar;" +
                                LIBDIR + "asm-tree-3.2.jar;" +
                                LIBDIR + "asm-util-3.2.jar;" +
                                LIBDIR + "antlr-2.7.7.jar;" +
                                LIBDIR + "mongo-java-driver-2.5.2.jar" +
                                " " +
                                "org.mca.qmass.console.ConsoleMain";

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }

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
