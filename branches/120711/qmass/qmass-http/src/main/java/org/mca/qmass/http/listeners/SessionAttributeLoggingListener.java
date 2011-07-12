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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:00:04
 */
public class SessionAttributeLoggingListener implements HttpSessionAttributeListener {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.debug("attributeAdded; " + httpSessionBindingEvent.getName() + ", " + httpSessionBindingEvent.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.debug("attributeRemoved; " + httpSessionBindingEvent.getName() + ", " + httpSessionBindingEvent.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.debug("attributeReplaced; " + httpSessionBindingEvent.getName()
                + ", " + httpSessionBindingEvent.getSession().getAttribute(httpSessionBindingEvent.getName()));
    }

}
