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

import com.sun.el.ValueExpressionLiteral;
import org.mca.qmass.console.service.ConsoleService;
import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.QMassGrid;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import java.lang.reflect.Method;

/**
 * User: malpay
 * Date: 04.Tem.2011
 * Time: 15:22:32
 */
public class QMassELContext extends ELContext {

    private QMass qmass;

    private ConsoleService consoleService;

    public QMassELContext(QMass qmass, ConsoleService consoleService) {
        this.qmass = qmass;
        this.consoleService = consoleService;
    }

    private final ELResolver resolver = new QMassELResolver();

    private final FunctionMapper functionMapper = new FunctionMapper() {

        @Override
        public Method resolveFunction(String prefix, String localName) {
            return null;
        }

    };

    private final VariableMapper variableMapper = new VariableMapper() {

        @Override
        public ValueExpression resolveVariable(String var) {
            if (consoleService.getId().equals(var)) {
                return new ValueExpressionLiteral(consoleService, Object.class);
            }
            // @TODO refactor this block
            String gridid = qmass.getId() + "/Grid/" + var;
            QMassGrid grid = (QMassGrid) qmass.getService(
                    gridid);
            if (grid == null) {
                grid = new QMassGrid(var, qmass);
            }
            return new ValueExpressionLiteral(grid, Object.class);
        }

        @Override
        public ValueExpression setVariable(String s, ValueExpression valueExpression) {
            return null;
        }

    };

    @Override
    public ELResolver getELResolver() {
        return resolver;
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return functionMapper;
    }

    @Override
    public VariableMapper getVariableMapper() {
        return variableMapper;
    }
}
