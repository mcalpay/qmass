package org.mca.qmass.cache;

import org.mca.qmass.core.Service;

import java.io.Serializable;
import java.util.List;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 10:52:48
 */
public interface QCache extends Service {

    QCache parent();

    List<QCache> children();

    Object getSilently(Serializable key);

    QCache put(Serializable key, Serializable value);
    
    QCache putSilently(Serializable key, Serializable value);

    QCache remove(Serializable key);

    QCache removeSilently(Serializable key);

    QCache clear();

    QCache clearSilently();

}
