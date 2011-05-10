package org.mca.qmass.core.event;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 11:08:17
 */
public class NOOPEventClosure implements EventClosure {

    private static EventClosure instance = new NOOPEventClosure();

    private NOOPEventClosure() {
    }

    public static EventClosure getInstance() {
        return instance;
    }

    @Override
    public Object execute(AbstractEvent event) throws Exception {
        return this;
    }
}
