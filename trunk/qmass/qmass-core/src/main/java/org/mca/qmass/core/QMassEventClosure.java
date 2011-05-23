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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.event.Event;
import org.mca.qmass.core.event.EventClosure;
import org.mca.qmass.core.event.EventHandler;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 11:00:43
 */
public class QMassEventClosure implements EventClosure {

    private static final Log logger = LogFactory.getLog(QMassEventClosure.class);

    private QMass qmass;

    public QMassEventClosure(QMass qmass) {
        this.qmass = qmass;
    }

    @Override
    public Object execute(Event event) throws Exception {
        Service service = qmass.getService(event.getServiceId());
        if(service == null && event.createServiceOnEvent()) {
            service = event.createService();
        }
        logger.debug(qmass.getClusterManager().getId() + ", " + qmass.getId() + " received; " + event + ", service : " + service);
        if (event.getId().equals(qmass.getId()) && service != null) {
            EventHandler handler = (EventHandler) Class.forName(event.getHandlerName()).newInstance();
            handler.handleEvent(qmass, service, event);
        }
        return this;
    }
}
