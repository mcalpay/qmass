package org.mca.qmass.console.service;

import org.mca.qmass.core.QMass;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 05.Tem.2011
 * Time: 15:45:51
 */
public class DefaultConsoleService implements ConsoleService {

    private boolean systemLogs = true;

    public DefaultConsoleService(QMass qmass) {
        qmass.registerService(this);
    }

    @Override
    public Serializable getId() {
        return "console";
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
}
