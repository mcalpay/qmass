package org.mca.qmass.event;

import org.mca.qmass.core.Service;

/**
 * User: malpay
 * Date: 11.May.2011
 * Time: 17:57:42
 * To change this templat use File | Settings | File Templates.
 */
public interface LogService extends Service {
    
    LogService changeLog(String clazz, String level);
    
}
