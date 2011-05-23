package org.mca.qmass.http.events;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:45:36
 */
public class AttributeRemoveEvent extends BindingEvent {

    public AttributeRemoveEvent(QMass qm, Service service, Serializable name, Serializable value) {
        super(qm, service, name, value);
    }
    
}
