package org.mca.qmass.core.ir;

import org.mca.qmass.core.cluster.ClusterManager;
import org.mca.qmass.core.cluster.DatagramClusterManager;

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
    public int getDefaultThreadWait() {
        return 500;
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
