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

import org.mca.qmass.grid.QMassGrid;

import javax.el.BeanELResolver;
import javax.el.ELContext;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 04.Tem.2011
 * Time: 16:36:14
 */
public class QMassELResolver extends BeanELResolver {

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if(base instanceof QMassGrid) {
            QMassGrid grid = (QMassGrid) base;
            context.setPropertyResolved(true);
            Serializable serializable = grid.get((Serializable) property);
            return serializable;
        }
        return super.getValue(context, base, property);
    }
}
