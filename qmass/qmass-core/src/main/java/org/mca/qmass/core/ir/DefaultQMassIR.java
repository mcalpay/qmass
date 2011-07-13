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
 * Time: 09:52:33
 */
public class DefaultQMassIR implements QMassIR {

    @Override
    public String getCluster() {
        return "localhost,6661,6670/";
    }

    @Override
    public boolean getReplicateUpdates() {
        return false;
    }

    @Override
    public boolean getReplicateInserts() {
        return false;
    }

    @Override
    public String getMulticastAddress() {
        return "";
    }

    @Override
    public int getMulticastReadPort() {
        return 4444;
    }

    @Override
    public int getMulticastWritePort() {
        return 4445;
    }

}
