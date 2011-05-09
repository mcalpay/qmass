package org.mca.qmass.event;

import org.junit.Test;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.NOOPService;

/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 10:44:13
 */
public class LogChangeEventTests {

    @Test
    public void changeLogLevels() throws Exception {
        QMass mass = QMass.getQMass();
        mass.sendEvent(new LogChangeEvent(mass, NOOPService.getInstance(), "class", "DEBUG"));
    }
}
