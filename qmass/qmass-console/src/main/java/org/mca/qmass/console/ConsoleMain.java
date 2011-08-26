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
import org.mca.qmass.core.QMass;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * User: malpay
 * Date: 05.Tem.2011
 * Time: 10:14:50
 */
public class ConsoleMain {

    private static final Log logger = LogFactory.getLog(ConsoleMain.class);

    public static void main(String... args) throws Exception {
        QMass qmass = QMass.getQMass();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        final Console console = new QConsole(qmass, System.out);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                console.end();
            }
        });

        while (console.running()) {
            String untrimmedLine = bufferedReader.readLine();
            console.evaluate(untrimmedLine);
        }

        qmass.end();
        Thread.sleep(1000);
    }

}
