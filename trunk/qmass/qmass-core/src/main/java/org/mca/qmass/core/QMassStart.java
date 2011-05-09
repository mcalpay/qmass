package org.mca.qmass.core;

import org.mca.ir.IR;
import org.mca.qmass.core.ir.DefaultQMassIR;

/**
 * User: malpay
 * Date: 06.May.2011
 * Time: 17:06:20
 */
public class QMassStart {

    public static void main(final String... args) throws Exception {
        String id = args[0];
        IR.put(id, new DefaultQMassIR() {
            @Override
            public String getCluster() {
                return args[1];
            }

            @Override
            public int getDefaultThreadWait() {
                return 100;
            }
        });
        QMass qm = QMass.getQMass(id);
        while(true) {
        }
    }

}
