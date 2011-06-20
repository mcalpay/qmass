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
package org.mca.qmass.http.qcache.services;

import org.mca.qmass.core.Service;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:15:43
 *
 * Raises and handles the events needed to synchronise sessions.
 */
public interface SessionEventsService extends Service {

    SessionEventsService doAttributeAdded(Serializable name, Serializable value);

    SessionEventsService doAttributeRemoved(Serializable name);

    SessionEventsService sync(HttpSession session);

    SessionEventsService attributeAdded(String name, Object value);

    SessionEventsService attributeRemoved(String name);

    SessionEventsService end();

    SessionEventsService checkForChangedAttributes(HttpSession session);
}
