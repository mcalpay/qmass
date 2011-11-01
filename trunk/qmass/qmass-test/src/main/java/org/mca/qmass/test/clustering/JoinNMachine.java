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
package org.mca.qmass.test.clustering;

import org.mca.qmass.core.QMass;
import org.mca.qmass.test.runner.ProcessRunner;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * User: malpay
 * Date: 21.10.2011
 * Time: 14:21
 */
public class JoinNMachine {

    public static void main(String... args) throws Exception {
        QMass.getQMass();
        final int numOfInstances = 5;
        final String LIBDIR = "F:/qmass/dependencies/";
        final String ARTIFACTSDIR = LIBDIR;
        final String outputDir = "F:/dists/";
        final String elConsole = "java -cp " +
                ARTIFACTSDIR + "qmass.jar;" +
                ARTIFACTSDIR + "qmass_test.jar;" +
                LIBDIR + "mongo-java-driver-2.5.2.jar" +
                " " +
                "org.mca.qmass.console.ConsoleMain";
        ProcessRunner pr = new ProcessRunner(elConsole, numOfInstances, outputDir);
        pr.initProcesses();
        System.err.println("waiting for " + numOfInstances + " instances to join.");
        long startTime = System.currentTimeMillis();
        pr.start();
        while (QMass.getQMass().getEventService().getCluster().length
                < numOfInstances) {
        }

        long endTime = System.currentTimeMillis();
        System.err.println("Setup time : " + (endTime - startTime));
        System.err.println("final cluster : " + Arrays.asList(QMass.getQMass().getEventService().getCluster()));

        Thread.sleep(10000);
        QMass.getQMass().end();
        pr.end();
    }

}
