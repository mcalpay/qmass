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

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

import org.mca.qmass.console.service.DefaultConsoleService;
import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.QMassGrid;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

/**
 * User: malpay
 * Date: 05.Tem.2011
 * Time: 09:45:57
 */
public class QMassELContextTests {

    private QMass qmass = QMass.getQMass();

    private ExpressionFactory expressionFactory = ExpressionFactory.newInstance();

    private ELContext elContext = new QMassELContext(qmass, new DefaultConsoleService(qmass));

    @Before
    public void configure() {
    }


    @Test
    public void testPutGetRemove() throws Exception {
        ValueExpression putExp = expressionFactory.createValueExpression(elContext, "${car.put('first','boo')}",
                Object.class);
        assertEquals(true, putExp.getValue(elContext));
        ValueExpression getExp = expressionFactory.createValueExpression(elContext, "${car.get('first')}",
                Object.class);
        assertEquals("boo", getExp.getValue(elContext));
        ValueExpression remExp = expressionFactory.createValueExpression(elContext, "${car.remove('first')}",
                Object.class);
        assertEquals("boo", remExp.getValue(elContext));
        assertEquals(null, getExp.getValue(elContext));

    }

    private QMassGrid getGrid(String var) {
        String gridid = qmass.getId() + "/Grid/" + var;
        QMassGrid grid = (QMassGrid) qmass.getService(gridid);
        if (grid == null) {
            grid = new QMassGrid(var, qmass);
        }
        return grid;
    }

}
