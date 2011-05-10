package org.mca.qmass.core.cluster;

import org.junit.Test;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.LogEventClosure;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 11:05:05
 */
public class MulticastClusterManagerTests implements Serializable {

    @Test
    public void testSendReceive() throws Exception {
        ClusterManager cm = new MulticastClusterManager();
        cm.sendEvent(new Event());
        cm.receiveEventAndDo(LogEventClosure.getInstance());
    }
}
