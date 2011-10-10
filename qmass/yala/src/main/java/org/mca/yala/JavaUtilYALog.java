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

import java.util.logging.*;

/**
 * User: malpay
 * Date: 06.10.2011
 * Time: 11:51
 */
public class JavaUtilYALog implements YALog {

    private Logger logger;

    public JavaUtilYALog(Logger logger) {
        this.logger = logger;
        for (Handler h : logger.getParent().getHandlers()) {
            h.setFormatter(new SimpleJavaUtilFormatter());
        }
    }

    private LogRecord newLogRecord(Level level, Object msg) {
        JavaUtilLogRecord logRecord = new JavaUtilLogRecord(level, msg.toString());
        logRecord.setLoggerName(logger.getName());
        return logRecord;
    }


    private LogRecord newLogRecord(Level level, Object msg, Throwable t) {
        LogRecord record = newLogRecord(level, msg);
        record.setThrown(t);
        return record;
    }

    @Override
    public void trace(Object msg) {
        logger.log(newLogRecord(Level.FINEST, msg));
    }

    @Override
    public void trace(Object msg, Throwable t) {
        logger.log(newLogRecord(Level.FINEST, msg, t));
    }

    @Override
    public void debug(Object msg) {
        logger.log(newLogRecord(Level.FINER, msg));
    }

    @Override
    public void debug(Object msg, Throwable t) {
        logger.log(newLogRecord(Level.FINER, msg, t));
    }

    @Override
    public void info(Object msg) {
        logger.log(newLogRecord(Level.INFO, msg));
    }

    @Override
    public void info(Object msg, Throwable t) {
        logger.log(newLogRecord(Level.INFO, msg, t));
    }

    @Override
    public void warn(Object msg) {
        logger.log(newLogRecord(Level.WARNING, msg));
    }

    @Override
    public void warn(Object msg, Throwable t) {
        logger.log(newLogRecord(Level.WARNING, msg, t));
    }

    @Override
    public void error(Object msg) {
        logger.log(newLogRecord(Level.SEVERE, msg));
    }

    @Override
    public void error(Object msg, Throwable t) {
        logger.log(newLogRecord(Level.SEVERE, msg, t));
    }

}
