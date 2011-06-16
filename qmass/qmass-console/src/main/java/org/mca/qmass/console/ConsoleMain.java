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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.QMassGrid;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 16.Haz.2011
 * Time: 13:26:58
 */
public class ConsoleMain {

    private static final Log logger = LogFactory.getLog(ConsoleMain.class);

    public static void main(String... args) throws Exception {
        QMassConsoleAppender appender = (QMassConsoleAppender)
                Logger.getRootLogger().getAppender("QCONSOLE");
        QMass qmass = QMass.getQMass();
        QMassGrid qg = new QMassGrid(qmass);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean runing = true;
        while (runing) {
            appender.print();
            System.out.print("> ");
            String line = bufferedReader.readLine().trim();
            try {
                if ("bye".equals(line)) {
                    runing = false;
                    System.out.println("bye bye");
                } else if (line.startsWith("put ")) {
                    String keyValue = line.substring("put ".length());
                    String key = keyValue.substring(0, keyValue.indexOf(" "));
                    String value = keyValue.substring(keyValue.indexOf(" ")).trim();
                    boolean ok = qg.put(key, value);
                    if (ok) {
                        System.out.println("put ok");
                    } else {
                        System.out.println("put failed");
                    }
                } else if (line.startsWith("get ")) {
                    String key = line.substring("get ".length());
                    Serializable value = qg.get(key);
                    System.out.println("get " + key + " returns '" + value + "'");
                } else if (!"".equals(line)) {
                    System.out.println("I dont understand '" + line + "'");
                }
            } catch (Exception e) {
                System.out.println("I dont understand '" + line + "'");
                logger.debug("Console error", e);
            }
        }                   

        qmass.end();
        appender.print();
    }

}
