package org.mca.qmass.console.service;

import org.mca.qmass.core.Service;

/**
 * User: malpay
 * Date: 05.Tem.2011
 * Time: 15:45:07
 */
public interface ConsoleService extends Service {

    void stopSystemLogs();

    void startSystemLogs();

    boolean systemLogs();

}
