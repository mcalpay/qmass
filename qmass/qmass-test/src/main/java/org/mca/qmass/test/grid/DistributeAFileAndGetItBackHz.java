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

import com.hazelcast.core.Hazelcast;
import org.mca.qmass.grid.node.GridData;
import org.mca.qmass.grid.node.MapGridDataAdapter;
import org.mca.qmass.test.runner.MainArgs;
import org.mca.qmass.test.runner.AbstractProcessRunner;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 13:37:16
 */
public class DistributeAFileAndGetItBackHz {

    private static final int CHUNKLENGTH = 128;

    private static final int NUMOFREADERS = 8;

    public static void main(String... args) throws Exception {
        System.setOut(new PrintStream(new FileOutputStream("D:\\work\\development\\MCA\\qmass_working_dir\\dists\\main.in")));
        final int numOfInstances = MainArgs.getNumberOfInstances(args);
        final String WRKDIR = "D:\\work\\development\\MCA\\qmass\\qmass\\classes\\artifacts\\qmass_working_dir\\";
        DistributeAFileAndGetItBackTemplate t = new DistributeAFileAndGetItBackTemplate() {

            @Override
            protected void endGrid() {
                Hazelcast.shutdownAll();
            }

            @Override
            protected void waitUntilGridIsReady() {
                while (Hazelcast.getCluster().getMembers().size() < getNumOfGridInstances() + 1) {
                }
            }

            @Override
            protected BufferedInputStream getInputFile() {
                BufferedInputStream bis = new BufferedInputStream( getClass().getResourceAsStream("/rock.jpg"));
                return bis;
            }

            @Override
            protected String getOutputDir() {
                return WRKDIR + "\\dists\\";
            }

            @Override
            protected AbstractProcessRunner getRunnerTemplate() {
                return new AbstractProcessRunner(numOfInstances, getOutputDir()) {

                    @Override
                    protected String getRunString() {
                        String elConsole = "java -cp " +
                                WRKDIR + "qmass_test.jar;" +
                                " " +
                                "org.mca.qmass.test.grid.DistributeAFileAndGetItBackHz";
                        System.err.println(elConsole);
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
