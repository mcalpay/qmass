package org.mca.qmass.web.ir;

import org.mca.qmass.core.ir.DefaultQMassIR;

/**
 * User: malpay
 * Date: 24.May.2011
 * Time: 10:25:56
 *
 * Configure the qmass instance.
 */
public class MyIR extends DefaultQMassIR {

    @Override
    public String getMulticastAddress() {
        return "230.0.0.1";
    }
    
}
