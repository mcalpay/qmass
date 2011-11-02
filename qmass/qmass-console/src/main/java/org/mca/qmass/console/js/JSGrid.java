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
package org.mca.qmass.console.js;

import org.mca.qmass.grid.Filter;
import org.mca.qmass.grid.Grid;
import org.mca.qmass.grid.node.GridNode;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * User: malpay
 * Date: 12.10.2011
 * Time: 11:05
 */
public class JSGrid implements Grid {

    private Grid delegate;

    public JSGrid(Grid delegate) {
        this.delegate = delegate;
    }

    @Override
    public Grid addGridNode(GridNode node) {
        return delegate.addGridNode(node);
    }

    @Override
    public Grid removeGridNode(GridNode node) {
        return delegate.removeGridNode(node);
    }

    @Override
    public void end() {
        delegate.end();
    }

    @Override
    public int compareTo(Object o) {
        return delegate.compareTo(o);
    }

    public void merge(Number key, Serializable value) {
        delegate.merge(key, value);
    }

    public Boolean put(Number key, Serializable value) {
        return delegate.put(key, value);
    }

    public Serializable get(Number key) {
        return delegate.get(key);
    }

    public Serializable remove(Number key) {
        return delegate.remove(key);
    }

    @Override
    public void merge(Serializable key, Serializable value) {
        delegate.merge(key, value);
    }

    @Override
    public Boolean put(Serializable key, Serializable value) {
        return delegate.put(key, value);
    }

    @Override
    public Serializable get(Serializable key) {
        return delegate.get(key);
    }

    @Override
    public Serializable remove(Serializable key) {
        return delegate.remove(key);
    }

    @Override
    public Set<Map.Entry<Serializable, Serializable>> filter(Filter filter) {
        return delegate.filter(filter);
    }

}
