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
package org.mca.qmass.core.event;

import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 11:08:17
 */
public class LogEventClosure implements EventClosure {

    private static final YALog logger = YALogFactory.getLog(LogEventClosure.class);

    private static EventClosure instance = new LogEventClosure();

    private LogEventClosure() {
    }

    public static EventClosure getInstance() {

        return instance;
    }

    @Override
    public Object execute(Event event) throws Exception {
        logger.debug("event : " + event);
        return this;
    }
}
