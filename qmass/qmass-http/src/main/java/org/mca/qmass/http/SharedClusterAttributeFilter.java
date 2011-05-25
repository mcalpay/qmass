package org.mca.qmass.http;

/**
 * User: malpay
 * Date: 25.May.2011
 * Time: 14:09:32
 *
 * Filter out all the attributes which are not annotated with @Shared 
 */
public class SharedClusterAttributeFilter implements ClusterAttributeFilter {

    @Override
    public boolean filtered(String name, Object value) {
        if (value != null) {
            final boolean shared = value.getClass().getAnnotation(Shared.class) != null;
            return !shared;
        }
        return true;
    }

}
