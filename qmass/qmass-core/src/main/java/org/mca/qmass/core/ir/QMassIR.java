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
package org.mca.qmass.core.ir;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 09:52:20
 */
public interface QMassIR {

    final char SEPARTOR = '/';

    final String DEFAULT = "default";

    final String QMASS_IR = "QMassIR";

    String getCluster();

    int getDefaultThreadWait();

    boolean getReplicateUpdates();

    boolean getReplicateInserts();

    String getMulticastAddress();

    int getMulticastReadPort();

    int getMulticastWritePort();
}
