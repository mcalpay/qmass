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
package org.mca.qmass.core.id;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 14.Haz.2011
 * Time: 15:19:52
 */
public class Id implements Serializable {

    private Integer id;

    private Serializable key;

    public Id(Integer id, Serializable key) {
        this.id = id;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Id ýd = (Id) o;

        if (!id.equals(ýd.id)) return false;
        if (!key.equals(ýd.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + key.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Id{" +
                "id=" + id +
                ", key=" + key +
                '}';
    }
}
