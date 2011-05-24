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
package org.mca.qmass.core;

import org.mca.ir.IR;
import org.mca.qmass.core.ir.DefaultQMassIR;

/**
 * User: malpay
 * Date: 06.May.2011
 * Time: 17:06:20
 */
public class QMassStart {

    public static void main(final String... args) throws Exception {
        String id = args[0];
        IR.put(id, "QMassIR", new DefaultQMassIR() {
            @Override
            public String getCluster() {
                return args[1];
            }

            @Override
            public int getDefaultThreadWait() {
                return 100;
            }
        });
        QMass qm = QMass.getQMass(id);
        while (true) {
        }
    }

}
