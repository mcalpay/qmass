package org.mca.qmass.jetty;

import java.io.PrintStream;

/**
 * User: malpay
 * Date: 30.May.2011
 * Time: 15:19:58
 */
public class Runner {

    public static void main(String[] args) throws Exception {
        int len = 5;
        if (args.length > 0) {
            len = Integer.valueOf(args[0]);
        }

        int port = 8080;
        if (args.length > 1) {
            port = Integer.valueOf(args[1]);
        }

        int i = 0;
        while (len > i) {
            System.out.println("Starting at port " + port);
            Process p = Runtime.getRuntime()
                    .exec("java -jar qmass_demo.jar " + (port));
            i++;
            port++;
        }

    }
}
