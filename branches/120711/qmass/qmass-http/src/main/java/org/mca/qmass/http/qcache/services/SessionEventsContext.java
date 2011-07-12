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

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:19:34
 *
 * May be removed, this service could be part of session wrapper.
 */
public class SessionEventsContext {

    private static ThreadLocal<SessionEventsService> currentInstance = new ThreadLocal<SessionEventsService>();

    public static SessionEventsService setCurrentInstance(SessionEventsService service) {
        currentInstance.set(service);
        return service;
    }

    public static SessionEventsService getCurrentInstance() {
        return currentInstance.get();
    }

    public static SessionEventsService invalidate() {
        SessionEventsService ses = getCurrentInstance();
        ses.end();
        currentInstance.set(null);
        return ses;
    }
}
