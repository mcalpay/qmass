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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: malpay
 * Date: 06.10.2011
 * Time: 11:51
 */
public class JavaUtilYALog implements YALog {

    private Logger logger;

    public JavaUtilYALog(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void trace(Object msg) {
        logger.finest(msg.toString());
    }

    @Override
    public void trace(Object msg, Throwable t) {
        logger.log(Level.FINEST, msg.toString(), t);
    }

    @Override
    public void debug(Object msg) {
        logger.finer(msg.toString());
    }

    @Override
    public void debug(Object msg, Throwable t) {
        logger.log(Level.FINER, msg.toString(), t);
    }

    @Override
    public void info(Object msg) {
        logger.info(msg.toString());
    }

    @Override
    public void info(Object msg, Throwable t) {
        logger.log(Level.INFO, msg.toString(), t);
    }

    @Override
    public void warn(Object msg) {
        logger.warning(msg.toString());
    }

    @Override
    public void warn(Object msg, Throwable t) {
        logger.log(Level.WARNING, msg.toString(), t);
    }

    @Override
    public void error(Object msg) {
        logger.severe(msg.toString());
    }

    @Override
    public void error(Object msg, Throwable t) {
        logger.log(Level.SEVERE, msg.toString(), t);
    }

}
