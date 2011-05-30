package org.mca.qmass.jetty;

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
        String jetty_home = System.getProperty("jetty.home", "F:\\qmass\\examples\\session_sharing\\war");
        int port = 8080;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        log.debug("starting server at port " + port);
        Server server = new Server(port);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/fastdog");
        webapp.setWar(jetty_home + "\\qmass.war");
        server.setHandler(webapp);
        server.start();
        server.join();
    }

}
