package org.mca.qmass.http.events;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 23.May.2011
 * Time: 11:44:49
 */
public class AttributeAddEvent extends BindingEvent {

    public AttributeAddEvent(QMass qm, Service service, Serializable name, Serializable value) {
        super(qm, service, name, value);
    }
    
}
