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

import java.io.Serializable;

/**
 * User: malpay
 * Date: 09.Haz.2011
 * Time: 14:24:57
 */
public interface GridNode extends Comparable {

    Boolean put(Serializable key, Serializable value);

    Serializable get(Serializable key);

    Serializable remove(Serializable key);

    GridNode end();
}
