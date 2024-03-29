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
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.awt.event.ActionEvent;
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

    private static final YALog logger = YALogFactory.getLog(QMass.class);

    private String output;

    private String input;

    private Console console;

    private ByteArrayOutputStream out;

    public ConsoleBean() {
        new StringBuffer();
        out = new ByteArrayOutputStream();
        QMass qmass = QMass.getQMass();
        logger.info("Trying to create console for : " + qmass);
        console = new QConsole(qmass, new PrintStream(out), true);
        output = out.toString();
    }

    public void handleCommand() {
        console.evaluate(input);
        output = out.toString();
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
