package org.mca.qmass.core.ir;

import org.mca.qmass.core.cluster.ClusterManager;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 09:52:20
 */
public interface QMassIR {

    static final char SEPARTOR = '/';

    static final String DEFAULT = "default";

    String getCluster();

    int getDefaultThreadWait();

    boolean getReplicateUpdates();

    boolean getReplicateInserts();

    String getMulticastAddress();

    int getMulticastReadPort();

    int getMulticastWritePort();
}
