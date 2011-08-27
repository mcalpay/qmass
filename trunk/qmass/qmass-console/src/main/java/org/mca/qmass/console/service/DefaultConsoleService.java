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
        return "@" + qmass.getClusterManager().getListening() +
                "\nCluster : " + Arrays.asList(qmass.getClusterManager().getCluster());
    }
}
