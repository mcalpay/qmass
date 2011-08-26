/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mca.qmass.http.listeners;

import org.mca.qmass.core.QMass;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 26.08.2011
 * Time: 12:48
 */
public class QMassEnderServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent se) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent se) {
        QMass.getQMass().end();
    }
}
