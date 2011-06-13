package org.mca.qmass.grid.ir;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 15:32:14
 */
public class DefaultQMassGridIR implements QMassGridIR {

    @Override
    public int getResponseTimeout() {
        return 1000;
    }

}
