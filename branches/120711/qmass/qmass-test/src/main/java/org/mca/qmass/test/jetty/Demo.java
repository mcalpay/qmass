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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * User: malpay
 * Date: 26.May.2011
 * Time: 15:37:46
 */
public class Demo {

    private static Log log = LogFactory.getLog(Demo.class);

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        String insPostFix = "";
        if (args.length > 1) {
            insPostFix = args[1];
        }

        log.debug("starting server at port " + port);
        Server server = new Server(port);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/qmassx" + insPostFix);
        webapp.setWar("war/qmass.war");
        server.setHandler(webapp);
        server.start();
        server.join();
    }

}
