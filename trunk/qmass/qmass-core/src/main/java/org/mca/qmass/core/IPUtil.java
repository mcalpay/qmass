package org.mca.qmass.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * User: malpay
 * Date: 18.08.2011
 * Time: 13:25
 */
public class IPUtil {

    public static String getLocalIpAsString() {
        String cloudIp = System.getenv().get("VCAP_APP_HOST");
        if(cloudIp != null && !cloudIp.isEmpty() ) {
            return cloudIp;
        }
        try {
            byte[] add = InetAddress.getLocalHost().getAddress();
            return add256IfNegative(add[0]) + "." + add256IfNegative(add[1]) + "." +
                   add256IfNegative(add[2]) + "." + add256IfNegative(add[3]);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static int add256IfNegative(byte b) {
        return (b >= 0 ? b : b + 256);
    }
}
