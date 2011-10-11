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
package org.mca.yala;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * User: malpay
 * Date: 10.10.2011
 * Time: 14:20
 */
public class SimpleJavaUtilFormatter extends Formatter {

    private MessageFormat template = new MessageFormat("{0,time}/{1} {2} @{3}\n");

    @Override
    public String format(LogRecord record) {
        String format = template.format(
            new Object[]{
                new Date(record.getMillis()),
                record.getLevel().getName(),
                record.getMessage(),
                record.getLoggerName()
            }
        );
        if (record.getThrown() != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos);
            record.getThrown().printStackTrace(printStream);
            printStream.close();
            format += baos.toString();
        }
        return format;
    }
}
