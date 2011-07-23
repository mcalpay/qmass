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
            Object d = ss.deSerialize(data);
            long deserEnd = System.currentTimeMillis();
            System.out.println("time spent : " + (deserEnd - start) +
                    ", serialization : " + (serEnd - start) +
                    ", deserialization : " + (deserEnd - serEnd) +
                    ", data length : " + data.length +
                    ", data : " + data);
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
