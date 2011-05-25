package org.mca.qmass.web.ir;

import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.http.ClusterAttributeFilter;
import org.mca.qmass.http.SharedClusterAttributeFilter;
import org.mca.qmass.http.ir.QMassHttpIR;

/**
 * User: malpay
 * Date: 24.May.2011
 * Time: 10:25:56
 *
 * Configure the qmass instance.
 */
public class MyIR extends DefaultQMassIR implements QMassHttpIR {

    private ClusterAttributeFilter attributeFilter = new SharedClusterAttributeFilter();

    @Override
    public String getMulticastAddress() {
        return "230.0.0.1";
    }

    @Override
    public ClusterAttributeFilter getClusterAttributeFilter() {
        return attributeFilter;
    }
}
