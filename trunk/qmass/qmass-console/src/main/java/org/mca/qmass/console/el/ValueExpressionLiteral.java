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

import org.mca.qmass.console.service.ConsoleService;

import javax.el.ELContext;
import javax.el.ValueExpression;

/**
 * User: malpay
 * Date: 21.08.2011
 * Time: 12:21
 */
public class ValueExpressionLiteral extends ValueExpression {

    private Object object;

    public ValueExpressionLiteral(Object object) {
        this.object = object;
    }

    @Override
    public Object getValue(ELContext elContext) {
        return object;
    }

    @Override
    public void setValue(ELContext elContext, Object o) {
    }

    @Override
    public boolean isReadOnly(ELContext elContext) {
        return true;
    }

    @Override
    public Class<?> getType(ELContext elContext) {
        return object.getClass();
    }

    @Override
    public Class<?> getExpectedType() {
        return object.getClass();
    }

    @Override
    public String getExpressionString() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return object.equals(o);
    }

    @Override
    public int hashCode() {
        return object.hashCode();
    }

    @Override
    public boolean isLiteralText() {
        return false;
    }
}
