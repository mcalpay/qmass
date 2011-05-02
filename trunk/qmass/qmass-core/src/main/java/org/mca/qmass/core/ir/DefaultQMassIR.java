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
}
