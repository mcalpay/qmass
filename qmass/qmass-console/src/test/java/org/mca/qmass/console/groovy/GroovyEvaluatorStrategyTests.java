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

import org.junit.Assert;
import org.junit.Test;
import org.mca.qmass.console.EvaluatorStrategy;
import org.mca.qmass.core.QMass;

/**
 * User: malpay
 * Date: 31.08.2011
 * Time: 18:02
 */
public class GroovyEvaluatorStrategyTests {

    @Test
    public void cantCallStaticMethods() throws Exception {
        EvaluatorStrategy es = new GroovyEvaluatorStrategy();
        boolean fail = true;
        try {
            es.evaluate("org.mca.qmass.core.QMass.getQMass()");
            //es.evaluate("System.out.println('test')");
        } catch (Exception e) {
            fail = false;
        }

        if(fail) Assert.fail();
    }

}
