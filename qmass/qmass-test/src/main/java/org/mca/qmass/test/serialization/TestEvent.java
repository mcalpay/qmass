package org.mca.qmass.test.serialization;

import org.mca.qmass.core.event.Event;

import java.util.List;

/**
 * User: malpay
 * Date: 20.Tem.2011
 * Time: 16:02:57
 */
public class TestEvent extends Event {

    private List values;

    public TestEvent(List values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "TestEvent{" +
                "values=" + values.toArray() +
                "} " + super.toString();
    }
}
