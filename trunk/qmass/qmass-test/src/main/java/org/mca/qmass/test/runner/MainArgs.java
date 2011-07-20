package org.mca.qmass.test.runner;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 09:29:59
 */
public class MainArgs {

    public static int getNumberOfInstances(String[] args) {
        int len = 4;
        if (args.length > 0) {
            len = Integer.valueOf(args[0]);
        }
        return len;
    }

}
