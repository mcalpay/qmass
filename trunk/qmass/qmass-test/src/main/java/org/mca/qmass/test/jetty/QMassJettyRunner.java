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
package org.mca.qmass.test.jetty;

import org.mca.qmass.test.runner.MainArgs;
import org.mca.qmass.test.runner.ProcessRunnerTemplate;

/**
 * User: malpay
 * Date: 30.May.2011
 * Time: 15:19:58
 */
public class QMassJettyRunner {

    public static void main(String[] args) throws Exception {
        int len = MainArgs.getNumberOfInstances(args);
        final int port = getPort(args);

        new ProcessRunnerTemplate(len, "f:/dists/") {
            private int i = 0;

            @Override
            protected String getRunString() {
                String result = "java -cp lib/ant-1.6.5.jar;lib/ecj-3.5.1.jar;" +
                        "lib/el-api-2.2.jar;lib/el-impl-2.2.jar;" +
                        "lib/jetty-continuation-7.2.0.v20101020.jar;" +
                        "lib/jetty-http-7.2.0.v20101020.jar;" +
                        "lib/jetty-io-7.2.0.v20101020.jar;" +
                        "lib/jetty-security-7.2.0.v20101020.jar;" +
                        "lib/jetty-server-7.2.0.v20101020.jar;" +
                        "lib/jetty-servlet-7.2.0.v20101020.jar;" +
                        "lib/jetty-util-7.2.0.v20101020.jar;" +
                        "lib/jetty-webapp-7.2.0.v20101020.jar;" +
                        "lib/jetty-xml-7.2.0.v20101020.jar;" +
                        "lib/jsp-2.1-glassfish-2.1.v20100127.jar;" +
                        "lib/jsp-api-2.1-glassfish-2.1.v20100127.jar;" +
                        "lib/log4j-1.2.16.jar;" +
                        "lib/commons-logging-1.1.1.jar;" +
                        "lib/servlet-api-2.5.jar;" +
                        "../../qmass.jar org.mca.qmass.jetty.QMassWebDemoApp " + (port + i) + " " + i;
                i++;
                return result;
            }

        }.start();


    }

    private static int getPort(String[] args) {
        int port = 8080;
        if (args.length > 1) {
            port = Integer.valueOf(args[1]);
        }
        return port;
    }
}
