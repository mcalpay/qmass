package org.mca.qmass.core.cluster.service;

import org.mca.qmass.core.Service;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * User: malpay
 * Date: 17.08.2011
 * Time: 23:15
 */
public class Listening implements Service {

    private InetSocketAddress socket;

    public Listening(InetSocketAddress socket) {
        this.socket = socket;
    }

    public InetSocketAddress getSocket() {
        return socket;
    }

    @Override
    public Serializable getId() {
        return Listening.class;
    }
}
