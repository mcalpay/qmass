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
        /* ClusterManager cm = new MulticastClusterManager();
        cm.start();
        cm.sendEvent(new Event());
        cm.receiveEventAndDo(LogEventClosure.getInstance());  */
    }
    
}
