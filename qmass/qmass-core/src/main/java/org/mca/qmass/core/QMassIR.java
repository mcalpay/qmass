package org.mca.qmass.core;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 09:52:20
 */
public interface QMassIR {

    static final char SEPARTOR = '/';

    static final String DEFAULT = "default";

    int getCapacity();

    String getCluster();

    int getDefaultThreadWait();

}
