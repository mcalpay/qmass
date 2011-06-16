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
package org.mca.qmass.console;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: malpay
 * Date: 16.Haz.2011
 * Time: 14:07:09
 */
public class QMassConsoleAppender extends ConsoleAppender {

    private Queue<LoggingEvent> events = new ConcurrentLinkedQueue<LoggingEvent>();

    @Override
    public void doAppend(LoggingEvent event) {
        events.add(event);
    }

    public QMassConsoleAppender print() {
        while (events.size() > 0) {
            super.doAppend(events.remove());
        }
        return this;
    }
}
