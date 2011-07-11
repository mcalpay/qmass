package org.mca.qmass.test;

import com.hazelcast.core.Hazelcast;

import java.util.Map;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 14:02:24
 */
public class DistributeAFileAndGetItBackHzMain {

    public static void main(String... args) throws Exception {
        Hazelcast.getMap("m");
        while (true) {
        }
    }

}
