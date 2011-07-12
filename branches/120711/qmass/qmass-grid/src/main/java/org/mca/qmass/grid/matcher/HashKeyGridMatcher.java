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
package org.mca.qmass.grid.matcher;

import org.mca.qmass.grid.node.GridNode;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 15:30:54
 */
public class HashKeyGridMatcher implements KeyGridMatcher {

    public GridNode match(Serializable key, List<GridNode> gridNodes) {
        return gridNodes.get(Math.abs(key.hashCode() % gridNodes.size()));
    }

}
