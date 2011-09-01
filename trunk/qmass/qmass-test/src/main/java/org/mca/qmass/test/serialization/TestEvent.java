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
package org.mca.qmass.test.serialization;

import org.mca.qmass.core.event.Event;

import java.util.List;

/**
 * User: malpay
 * Date: 20.Tem.2011
 * Time: 16:02:57
 */
public class TestEvent extends Event {

    private List values;

    public TestEvent(List values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "TestEvent{" +
                "values=" + values.toArray() +
                "} " + super.toString();
    }
}
