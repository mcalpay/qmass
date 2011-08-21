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

import org.mca.qmass.core.QMass;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * User: malpay
 * Date: 21.08.2011
 * Time: 07:48
 */
@ManagedBean
@SessionScoped
public class ConsoleBean implements Serializable {

    private String output;

    private String input;

    private Console console;

    private ByteArrayOutputStream out;

    public ConsoleBean() {
        new StringBuffer();
        out = new ByteArrayOutputStream();
        console = new QConsole(QMass.getQMass(), new PrintStream(out));
        output = out.toString();
    }

    public void handleCommand() {
        console.evaluate(input);
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
