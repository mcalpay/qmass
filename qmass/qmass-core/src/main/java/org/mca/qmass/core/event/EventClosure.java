package org.mca.qmass.core.event;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 10:53:37
 */
public interface EventClosure {
    Object execute(Event event) throws Exception;
}
