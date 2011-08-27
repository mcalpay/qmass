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
package org.mca.qmass.console.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.ui.Console;
import org.mca.qmass.console.EvaluatorStrategy;
import org.mca.qmass.console.service.ConsoleService;
import org.mca.qmass.core.QMass;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * User: malpay
 * Date: 26.08.2011
 * Time: 11:21
 */
public class GroovyEvaluatorStrategy implements EvaluatorStrategy {

    private static ResourceBundle bundle = ResourceBundle.getBundle("label", Locale.ENGLISH);

    private Binding binding;

    private GroovyShell shell;

    public GroovyEvaluatorStrategy(QMass qmass) {
        binding = new Binding();
        binding.setVariable("console", qmass.getService(ConsoleService.class));
        binding.setVariable("help", bundle.getString("console.help"));
        binding.setVariable("welcome", bundle.getString("console.welcome")+ "\n" +
                ((ConsoleService)qmass.getService(ConsoleService.class)).printClusterInfo());
        shell = new GroovyShell(binding);
    }

    @Override
    public Object evaluate(String command) {
        return shell.evaluate(command);
    }
}
