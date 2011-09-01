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
package org.mca.qmass.core.cluster.service;

import org.mca.qmass.core.Service;
import org.mca.qmass.core.cluster.EventManager;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 27.Tem.2011
 * Time: 15:14:28
 */
public interface EventService extends EventManager, DiscoveryService, Service {

    void sendEvent(Event event);

    void sendEvent(InetSocketAddress to, Event event);

    InetSocketAddress getListening();

    void start();

    void end() throws IOException;

}
