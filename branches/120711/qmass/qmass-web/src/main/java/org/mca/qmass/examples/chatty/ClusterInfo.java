package org.mca.qmass.examples.chatty;

import org.mca.qmass.core.QMass;
import org.mca.qmass.core.Service;
import org.mca.qmass.http.qcache.services.SessionEventsService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: malpay
 * Date: 31.May.2011
 * Time: 16:37:01
 */
@ManagedBean
@RequestScoped
public class ClusterInfo implements Serializable {

    private List services = new ArrayList();

    public List getServices() {
        if (services == null || services.size() == 0) {
            Collection<Service> seses = QMass.getQMass("examples").getServices();
            for (Object ses : seses) {
                if (ses instanceof SessionEventsService) {
                    services.add(ses);
                }
            }
        }
        return services;
    }

    public void setServices(List services) {
        this.services = services;
    }

}
