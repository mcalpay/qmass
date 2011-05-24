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
package org.mca.qmass.http;

import org.mca.qmass.core.QMass;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:04:23
 */
public class QMassContext {

    private static ThreadLocal<QMass> currentInstance = new ThreadLocal<QMass>();

    public static QMass setCurrentInstance(QMass qmass) {
        currentInstance.set(qmass);
        return qmass;
    }

    public static QMass getCurrentInstance() {
        return currentInstance.get();
    }

}
