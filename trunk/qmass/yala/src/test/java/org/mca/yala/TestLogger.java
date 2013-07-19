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

import java.io.Console;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: malpay
 * Date: 16.07.2013
 * Time: 06:56
 */
public class TestLogger {
    
    public static void main(String... args) throws Exception {
        Logger logger = Logger.getLogger(TestLogger.class .getName());
        for (Handler h : logger.getParent().getHandlers()) {
            System.out.println(h);
            if(h instanceof ConsoleHandler) {
                h.setLevel(Level.FINER);
            }
        }
        logger.info("info");
        logger.setLevel(Level.FINER);
        logger.finer("debug");
    }
}
