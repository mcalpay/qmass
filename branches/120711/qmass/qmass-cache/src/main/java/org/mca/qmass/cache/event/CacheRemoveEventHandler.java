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
package org.mca.qmass.cache.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.cache.QCache;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventHandler;
import org.mca.qmass.core.QMass;

import java.nio.ByteBuffer;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 12:10:05
 */
public class CacheRemoveEventHandler implements EventHandler {

    private final static Log logger = LogFactory.getLog(CacheRemoveEventHandler.class);

    @Override
    public EventHandler handleEvent(QMass qmass, Service service, Event event) {
        CacheRemoveEvent cre = (CacheRemoveEvent) event;
        QCache qcache = (QCache) service;
        if (cre.getCacheKey() == null) {
            qcache.clearSilently();
        } else {
            qcache.removeSilently(cre.getCacheKey());
        }
        return this;
    }
}
