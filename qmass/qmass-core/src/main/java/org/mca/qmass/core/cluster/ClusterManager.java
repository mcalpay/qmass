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

import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 15:56:13
 */
public interface ClusterManager {

    ClusterManager sendEvent(Event event) throws IOException;

    ClusterManager receiveEventAndDo(EventClosure closure) throws Exception;

    ClusterManager end() throws IOException;

    ClusterManager start();

    Serializable getId();

    InetSocketAddress getListeningAt();

    ClusterManager safeSendEvent(InetSocketAddress to, Event event);

    ClusterManager addToCluster(InetSocketAddress listeningAt);

    ClusterManager removeFromCluster(InetSocketAddress who);

    InetSocketAddress[] getCluster();

}
