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

import java.io.Serializable;

/**
 * User: malpay
 * Date: 11.May.2011
 * Time: 17:58:42
 */
public class DefaultLogService implements LogService {

    private Serializable id;

    private QMass mass;

    public Serializable getId() {
        return id;
    }

    public DefaultLogService(Serializable id, QMass mass) {
        this.id = id;
        this.mass = mass;
        mass.registerService(this);
    }

    @Override
    public LogService changeLog(String clazz, String level) {
        mass.sendEvent(new LogChangeEvent(mass, this, clazz, level));
        return this;
    }
}
