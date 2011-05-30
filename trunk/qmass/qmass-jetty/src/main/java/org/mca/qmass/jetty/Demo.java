package org.mca.qmass.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * User: malpay
 * Date: 26.May.2011
 * Time: 15:37:46
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        String jetty_home = System.getProperty("jetty.home", "F:\\qmass\\examples\\session_sharing\\war");

        Server server = new Server(8080);

        // he could play the guitar just like ringing bell
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/fastdog");
        webapp.setWar(jetty_home + "\\qmass.war");
        server.setHandler(webapp);
       

        server.start();
        server.join();
    }

}
