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
package org.mca.qmass.persistence;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 17.10.2011
 * Time: 11:22
 */
public class Tuple implements Serializable {

    private String type;

    private Serializable key;

    private Serializable value;

    public Tuple(String type, Serializable key) {
        this.type = type;
        this.key = key;
    }

    public Tuple(String type, Serializable key, Serializable value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }

    public Serializable getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public Serializable getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple that = (Tuple) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "type='" + type + '\'' +
                ", key=" + key +
                '}';
    }
}
