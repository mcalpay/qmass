package org.mca.qmass.core.event;

import org.mca.qmass.core.Service;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 06.May.2011
 * Time: 14:44:20
 */
public class NOOPService implements Service {

    public static final Serializable NOOP = "NOOP";

    private static NOOPService instance = new NOOPService();

    private NOOPService() {
    }

    public static NOOPService getInstance() {
        return instance;
    }

    @Override
    public Serializable getId() {
        return NOOP;
    }
}
