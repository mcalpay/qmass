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
