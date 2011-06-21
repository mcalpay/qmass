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
package org.mca.qmass.http.qcache.filters;

import org.mca.qmass.core.QMass;
import org.mca.qmass.http.filters.AbstractQMassFilter;
import org.mca.qmass.http.filters.HttpSessionWrapper;
import org.mca.qmass.http.qcache.services.DefaultSessionEventsService;
import org.mca.qmass.http.qcache.services.SessionEventsContext;
import org.mca.qmass.http.qcache.services.SessionEventsService;

import javax.servlet.http.HttpServletRequest;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 10:21:41
 * <p/>
 * Cache based session attribute filter
 */
public class QMassFilter extends AbstractQMassFilter {

    @Override
    protected void onInit() {

    }

    // before finishing up check the hashes and send events for changed values

    public void doAfterChain(HttpServletRequest servletRequest, HttpSessionWrapper wrapper) {
        SessionEventsContext.getCurrentInstance().checkForChangedAttributes(
                ((HttpServletRequest) servletRequest).getSession());
    }

    public HttpSessionWrapper wrapSession(HttpServletRequest servletRequest) {
        return new AttributeTrackingSessionWrapper(servletRequest, getAttributeFilter());
    }

    public void doBeforeChain(HttpServletRequest request, String qmassid, QMass mass) {
        SessionEventsService ses = (SessionEventsService) mass.getService(qmassid);
        if (ses == null) {
            SessionEventsContext.setCurrentInstance(
                    new DefaultSessionEventsService(qmassid,
                            getQMass()));
        } else {
            SessionEventsContext.setCurrentInstance(ses);
            ses.sync(request.getSession());
        }
    }

}
