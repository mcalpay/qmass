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

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 17.08.2011
 * Time: 23:15
 */
public class Listening implements Service {

    private InetSocketAddress socket;

    public Listening(InetSocketAddress socket) {
        this.socket = socket;
    }

    public InetSocketAddress getSocket() {
        return socket;
    }

    @Override
    public Serializable getId() {
        return Listening.class;
    }
}
