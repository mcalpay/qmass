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
 * Date: 10.10.2011
 * Time: 10:00
 */
public class YALogManager {

    public static void setLoggerLevel(String clazz, String level) {
        Logger logger = Logger.getLogger(clazz);
        if ("DEBUG".equals(level)) {
            logger.setLevel(Level.FINER);
        } else if ("WARN".equals(level)) {
            logger.setLevel(Level.WARNING);
        } else if ("INFO".equals(level)) {
            logger.setLevel(Level.INFO);
        } else if ("TRACE".equals(level)) {
            logger.setLevel(Level.FINEST);
        } else if ("ERROR".equals(level)) {
            logger.setLevel(Level.SEVERE);
        }
    }

}
