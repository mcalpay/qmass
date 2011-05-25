package org.mca.qmass.http;

/**
 * User: malpay
 * Date: 25.May.2011
 * Time: 14:02:57
 */
public interface ClusterAttributeFilter {

    boolean filtered(String name, Object value);

}
