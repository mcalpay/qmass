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

import org.mca.qmass.console.js.JSEvaluatorStrategy;
import org.mca.qmass.console.service.ConsoleService;
import org.mca.qmass.console.service.DefaultConsoleService;
import org.mca.qmass.core.QMass;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.*;

/**
 * User: malpay
 * Date: 21.08.2011
 * Time: 08:52
 */
public class QConsole implements Console {

    private static final YALog logger = YALogFactory.getLog(QConsole.class);

    private boolean running = true;

    private ConsolePrinter printer;

    private boolean echoCommand = false;

    private ConsoleService consoleService;

    private EvaluatorStrategy evaluatorStrategy;

    public QConsole(QMass qmass, PrintStream out) {
        this(qmass, out, false);
    }

    public QConsole(QMass qmass, PrintStream out, boolean echoCommand) {
        this.echoCommand = echoCommand;
        consoleService = new DefaultConsoleService(qmass);
        printer = new QConsolePrinter(out);
        logger.info("Trying to create JSEvaluatorStrategy for : " + qmass);
        evaluatorStrategy = new JSEvaluatorStrategy(qmass);
        println(evaluatorStrategy.evaluate("welcome").toString());
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
                } else if ("".equals(line)) {
                    println("");
                } else {
                    println("" + evaluatorStrategy.evaluate(line));
                }
            } catch (Exception e) {
                logger.error("Console error", e);
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
