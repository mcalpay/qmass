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
package org.mca.qmass.console.el;

import org.mca.qmass.console.EvaluatorStrategy;
import org.mca.qmass.console.service.ConsoleService;
import org.mca.qmass.console.service.DefaultConsoleService;
import org.mca.qmass.core.QMass;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 26.08.2011
 * Time: 10:46
 */
public class ELEvaluatorStrategy implements EvaluatorStrategy {

    private ExpressionFactory expressionFactory;

    private ELContext elContext;

    public ELEvaluatorStrategy(QMass qmass) {
        ConsoleService consoleService = (ConsoleService) qmass.getService(ConsoleService.class);
        expressionFactory = ExpressionFactory.newInstance();
        elContext = new QMassELContext(qmass, consoleService);
    }

    @Override
    public Object evaluate(String command) {
        ValueExpression valueExpression =
                expressionFactory.createValueExpression(elContext, "#{" + command + "}",
                        Object.class);
        return valueExpression.getValue(elContext);
    }
}
