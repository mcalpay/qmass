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
package org.mca.qmass.console.js;

import org.mca.qmass.console.EvaluatorStrategy;
import org.mca.qmass.console.service.ConsoleService;
import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.DefaultGrid;

import javax.script.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * User: malpay
 * Date: 12.10.2011
 * Time: 09:59
 */
public class JSEvaluatorStrategy implements EvaluatorStrategy {

    private static ResourceBundle bundle = ResourceBundle.getBundle("label", Locale.ENGLISH);

    private ScriptEngine engine;

    public JSEvaluatorStrategy(QMass qmass) {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("JavaScript");
        engine.put("console", qmass.getService(ConsoleService.class));
        engine.put("help", bundle.getString("console.help"));
        engine.put("welcome", bundle.getString("console.welcome") + "\n" +
                ((ConsoleService) qmass.getService(ConsoleService.class)).printClusterInfo());
    }

    @Override
    public Object evaluate(String command) {
        ensureNoBlackCommandsPass(command);
        try {
            Object eval = engine.eval(command);
            scanForGridBindingsAndReplace();
            return eval;
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private void scanForGridBindingsAndReplace() {
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        List<String> defaultGridKeys = new ArrayList<String>();
        for (String key : bindings.keySet()) {
            Object o = bindings.get(key);
            if (o instanceof DefaultGrid) {
                defaultGridKeys.add(key);
            }
        }

        for (String key : defaultGridKeys) {
            DefaultGrid dg = (DefaultGrid) bindings.get(key);
            bindings.put(key, new JSGrid(dg));
        }
    }

    private void ensureNoBlackCommandsPass(String command) {
        if (command.contains("new")) {
            throw new RuntimeException("new is not available");
        } else if (command.contains("import")) {
            throw new RuntimeException("you can't import");
        } else if (command.contains("java.lang")) {
            throw new RuntimeException("java.lang is not available");
        }
    }

}
