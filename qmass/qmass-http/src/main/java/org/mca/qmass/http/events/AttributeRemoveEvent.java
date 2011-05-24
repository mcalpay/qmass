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
package org.mca.qmass.http.events;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.http.services.DefaultSessionEventsService;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:45:36
 */
public class AttributeRemoveEvent extends BindingEvent {

    public AttributeRemoveEvent(QMass qm, Service service, Serializable name, Serializable value) {
        super(qm, service, name, value);
    }

    public AttributeRemoveEvent(QMass qm, Service service, Serializable name) {
        super(qm, service, name, null);
    }
}
