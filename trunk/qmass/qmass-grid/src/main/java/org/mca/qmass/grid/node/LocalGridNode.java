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
package org.mca.qmass.grid.node;

import org.mca.qmass.grid.matcher.GridKeyManager;
import org.mca.qmass.persistence.FilterPredicate;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 14:26:07
 */
public class LocalGridNode implements GridNode, TargetSocket {

    protected final YALog log = YALogFactory.getLog(getClass());

    private Map<Serializable, Serializable> dataMap = new ConcurrentHashMap<Serializable, Serializable>();

    private InetSocketAddress targetSocket;

    public LocalGridNode(InetSocketAddress targetSocket) {
        this.targetSocket = targetSocket;
    }

    @Override
    public void merge(Serializable key, Serializable value) {
        Map<Serializable, Integer> keyMap = (Map<Serializable, Integer>) dataMap.get(key);
        keyMap.putAll((Map<? extends Serializable, ? extends Integer>) value);
        log.debug("merged " + key + ", " + value + ", " + keyMap);
    }

    public Boolean put(Serializable key, Serializable value) {
        try {
            dataMap.put(key, value);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("error for key " + key + ", " + value, e);
            return Boolean.FALSE;
        }
    }

    public Serializable get(Serializable key) {
        return dataMap.get(key);
    }

    @Override
    public Serializable remove(Serializable key) {
        return dataMap.remove(key);
    }

    @Override
    public List filter(FilterPredicate filter) {
        List result = new ArrayList();
        Set<Map.Entry<Serializable, Serializable>> entries = dataMap.entrySet();
        for(Map.Entry<Serializable, Serializable> entry : entries) {
            if(!entry.getKey().equals(GridKeyManager.QMASS_KEY_MAP) && filter.filterInToResults(entry.getValue())) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    @Override
    public InetSocketAddress getTargetSocket() {
        return targetSocket;
    }

    @Override
    public void end() {
        dataMap = null;
        targetSocket = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        TargetSocket that = (TargetSocket) o;
        if (!targetSocket.equals(that.getTargetSocket())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return targetSocket.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        return new Integer(this.hashCode()).compareTo(new Integer(o.hashCode()));
    }

    @Override
    public String toString() {
        return "LocalGridNode{" +
                "targetSocket=" + targetSocket +
                '}';
    }
}
