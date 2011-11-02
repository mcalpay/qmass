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
package org.mca.qmass.test.serialization;

import org.mca.qmass.core.serialization.JavaSerializationStrategy;
import org.mca.qmass.core.serialization.SerializationStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: malpay
 * Date: 20.Tem.2011
 * Time: 16:00:44
 */
public class SerializeDeserializePerformance {

    public static void main(String... args) throws Exception {
        List testObjects = createRandomTestObjects(100);
        SerializationStrategy ss = getSerializationStrategy();
        for (Object o : testObjects) {
            long start = System.currentTimeMillis();
            byte[] data = ss.serialize(o);
            long serEnd = System.currentTimeMillis();
            ss.deSerialize(data);
            long deserEnd = System.currentTimeMillis();
            System.out.println("time spent : " + (deserEnd - start) +
                    ", serialization : " + (serEnd - start) +
                    ", deserialization : " + (deserEnd - serEnd) +
                    ", data length : " + data.length +
                    ", data : " + Arrays.toString(data));
        }
    }

    private static SerializationStrategy getSerializationStrategy() {
        return new JavaSerializationStrategy();
    }

    private static List createRandomTestObjects(int num) throws Exception {
        List list = new ArrayList();
        for (int i = 1; i <= num; i++) {
            //@TODO Randomize
            Thread.sleep(100);
            list.add(new TestEvent(Arrays.asList(new Date(), new Date(), new Date(), new Date(), new Date())));
        }
        return list;
    }

}
