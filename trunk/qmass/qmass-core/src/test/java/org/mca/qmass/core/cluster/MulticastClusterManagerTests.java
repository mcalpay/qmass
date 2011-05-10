package org.mca.qmass.core.cluster;

import org.junit.Before;
import org.junit.Test;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.NOOPEventClosure;

import static junit.framework.Assert.*;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 11:05:05
 */
public class MulticastClusterManagerTests {

    @Test
    public void testReceiveEmpty() throws Exception {
        ClusterManager cm = new MulticastClusterManager();
        cm.receiveEvent(NOOPEventClosure.getInstance());
    }
}
