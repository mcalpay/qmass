package org.mca.qmass.core.cluster;

import org.mca.qmass.core.event.EventClosure;

/**
 * User: malpay
 * Date: 01.Agu.2011
 * Time: 13:55:31
 */
public interface EventManager {

    void receiveEventAndDo(EventClosure closure) throws Exception;

}
