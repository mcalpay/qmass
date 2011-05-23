package org.mca.qmass.http;

import org.mca.qmass.core.QMass;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:04:23
 */
public class QMassContext {

    private static ThreadLocal<QMass> currentInstance = new ThreadLocal<QMass>();

    public static QMass setCurrentInstance(QMass qmass) {
        currentInstance.set(qmass);
        return qmass;
    }

    public static QMass getCurrentInstance() {
        return currentInstance.get();
    }

}
