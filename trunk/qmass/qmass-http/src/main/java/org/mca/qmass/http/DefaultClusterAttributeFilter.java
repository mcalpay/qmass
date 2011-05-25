package org.mca.qmass.http;

/**
 * Created by IntelliJ IDEA.
 * User: malpay
 * Date: 25.May.2011
 * Time: 14:08:50
 * To change this template use File | Settings | File Templates.
 */
public class DefaultClusterAttributeFilter implements ClusterAttributeFilter {
    @Override
    public boolean filtered(String name, Object value) {
        return name.startsWith("com.sun.faces");
    }
}
