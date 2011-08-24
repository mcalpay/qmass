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
import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * User: malpay
 * Date: 21.08.2011
 * Time: 08:52
 */
public class QConsole implements Console {

    private static final Log logger = LogFactory.getLog(QConsole.class);

    private boolean running = true;

    private ConsolePrinter printer;

    private ConsoleService consoleService;

    private ExpressionFactory expressionFactory;

    private ELContext elContext;

    private static ResourceBundle bundle = ResourceBundle.getBundle("label", Locale.ENGLISH);

    private boolean echoCommand = false;

    public QConsole(QMass qmass, PrintStream out) {
        this(qmass, out, false);
    }

    public QConsole(QMass qmass, PrintStream out, boolean echoCommand) {
        this.echoCommand = echoCommand;
        consoleService = new DefaultConsoleService(qmass);
        expressionFactory = ExpressionFactory.newInstance();
        elContext = new QMassELContext(qmass, consoleService);
        QMassConsoleAppender appender = (QMassConsoleAppender)
                Logger.getRootLogger().getAppender("QCONSOLE");
        if (appender == null) {
            appender = new QMassConsoleAppender();
        }

        printer = new QConsolePrinter(out, appender);

        println(bundle.getString("console.welcome"));
    }

    @Override
    public void evaluate(String untrimmedLine) {
        if (echoCommand) {
            printer.print(untrimmedLine);
        }

        if (running) {
            String line = (untrimmedLine != null) ? untrimmedLine.trim() : "";
            try {
                if ("bye".equals(line)) {
                    println("bye bye");
                    end();
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
    }

    @Override
    public void end() {
        running = false;
    }

    @Override
    public boolean running() {
        return running;
    }

    void println(String text) {
        if (consoleService.systemLogs()) {
            printer.printLogs();
        }

        if (!text.isEmpty()) {
            printer.printWithPrompt(text);
        }

        printer.prompt();
    }

}
