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
import org.mca.qmass.console.el.QMassELContext;
import org.mca.qmass.console.service.ConsoleService;
import org.mca.qmass.console.service.DefaultConsoleService;
import org.mca.qmass.core.QMass;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * User: malpay
 * Date: 05.Tem.2011
 * Time: 10:14:50
 */
public class ELConsoleMain {

    private static final Log logger = LogFactory.getLog(SimpleConsoleMain.class);

    private static QMassConsoleAppender appender;

    private static ResourceBundle bundle = ResourceBundle.getBundle("label", Locale.ENGLISH);

    private static ConsoleService consoleService;

    public static void main(String... args) throws Exception {
        appender = (QMassConsoleAppender)
                Logger.getRootLogger().getAppender("QCONSOLE");

        QMass qmass = QMass.getQMass();
        consoleService = new DefaultConsoleService(qmass);
        ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
        ELContext elContext = new QMassELContext(qmass, consoleService);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        final boolean[] runing = {true};
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                runing[0] = false;
            }
        });

        appender.print();
        println(bundle.getString("console.welcome"));

        while (runing[0]) {
            System.out.print("> ");
            String untrimmedLine = bufferedReader.readLine();
            String line = (untrimmedLine != null) ? untrimmedLine.trim() : "";
            try {
                if ("bye".equals(line)) {
                    runing[0] = false;
                    println("bye bye");
                } else if ("help".equals(line)) {
                    println(bundle.getString("console.help.put") +
                            bundle.getString("console.help.wrap.quotes") +
                            bundle.getString("console.help.get") +
                            bundle.getString("console.help.remove") +
                            bundle.getString("console.help.logs"));
                } else if ("".equals(line)) {
                    println("");
                } else {
                    ValueExpression valueExpression =
                            expressionFactory.createValueExpression(elContext, "${" + line + "}",
                                    Object.class);
                    println("returns : " + valueExpression.getValue(elContext));
                }
            } catch (Exception e) {
                logger.debug("Console error", e);
                println("command failed '" + line + "'");
            }
        }

        qmass.end();
    }

    private static void println(String text) {
        if (consoleService.systemLogs()) {
            System.out.println("[QMassConsole] Start system logs;");
            appender.print();
            System.out.println("[QMassConsole] End system logs;");
        }
        
        if (!text.isEmpty()) {
            System.out.println("[QMassConsole] " + text + "\n");
        }
    }

}
