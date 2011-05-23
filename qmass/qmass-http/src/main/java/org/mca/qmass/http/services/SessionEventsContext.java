package org.mca.qmass.http.services;

import org.mca.qmass.core.QMass;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:19:34
 */
public class SessionEventsContext {

    private static ThreadLocal<SessionEventsService> currentInstance = new ThreadLocal<SessionEventsService>();

      public static SessionEventsService setCurrentInstance(SessionEventsService service) {
          currentInstance.set(service);
          return service;
      }

      public static SessionEventsService getCurrentInstance() {
          return currentInstance.get();
      }

}
