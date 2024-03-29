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
package org.mca.qmass.core.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: malpay
 * Date: 20.Tem.2011
 * Time: 16:06:54
 */
public class JavaSerializationStrategy implements SerializationStrategy {

    @Override
    public Object deSerialize(byte[] bytes) {
        try {
            return new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(bos).writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bos.toByteArray();
    }

}
