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
package org.mca.qmass.console.service;

import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.QMassGrid;

import java.io.Serializable;
import java.util.Arrays;

/**
 * User: malpay
 * Date: 05.Tem.2011
 * Time: 15:45:51
 */
public class DefaultConsoleService implements ConsoleService {

    private boolean systemLogs = true;

    private QMass qmass;

    public DefaultConsoleService(QMass qmass) {
        this.qmass = qmass;
        qmass.registerService(this);
    }

    @Override
    public Serializable getId() {
        return ConsoleService.class;
    }

    @Override
    public void stopSystemLogs() {
        systemLogs = false;
    }

    @Override
    public void startSystemLogs() {
        systemLogs = true;
    }

    @Override
    public boolean systemLogs() {
        return systemLogs;
    }

    @Override
    public QMassGrid getMap(String var) {
        String gridid = QMassGrid.class + "/" + var;
        QMassGrid grid = (QMassGrid) qmass.getService(
                gridid);
        if (grid == null) {
            grid = new QMassGrid(var, qmass);
        }
        return grid;
    }

    @Override
    public String printClusterInfo() {
        return "@" + qmass.getEventService().getListening() +
                "\nCluster : " + Arrays.asList(qmass.getEventService().getCluster());
    }
}
