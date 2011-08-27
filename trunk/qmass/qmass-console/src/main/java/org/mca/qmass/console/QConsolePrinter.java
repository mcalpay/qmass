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

import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * User: malpay
 * Date: 21.08.2011
 * Time: 09:28
 */
public class QConsolePrinter implements ConsolePrinter {

    private PrintStream out;

    private QMassConsoleAppender appender;

    public QConsolePrinter(PrintStream out) {
        this.out = out;
        appender = (QMassConsoleAppender)
                Logger.getRootLogger().getAppender("QCONSOLE");
    }

    @Override
    public void printLogs() {
        if (appender != null && appender.hasEvents()) {
            out.println("[QMassConsole] Start system logs;");
            appender.print();
            out.println("[QMassConsole] End system logs;");
        }
    }

    @Override
    public void print(String text) {
        out.println(text + "\n");
    }

    @Override
    public void printWithPrompt(String text) {
        out.println("  " + text.replaceAll("\n", "\n  ").replaceAll("\t", "  ") + "\n");
    }

    @Override
    public void prompt() {
        out.print("> ");
    }

}
