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
package org.mca.qmass.event;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.core.event.Event;

/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 10:35:52
 */
public class LogChangeEvent extends Event {
    private String clazz;

    private String level;

    public LogChangeEvent(QMass qm, Service service, String clazz, String level) {
        super(qm, service, LogChangeEventHandler.class);
        this.clazz = clazz;
        this.level = level;
    }

    public String getClazz() {
        return clazz;
    }

    public String getLevel() {
        return level;
    }
}
