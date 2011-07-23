package org.mca.qmass.core.serialization;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * User: malpay
 * Date: 20.Tem.2011
 * Time: 15:50:53
 */
public class JavaSerializationStrategyTests {

    @Test
    public void serializeDeserializeAnArrayList() throws Exception {
        SerializationStrategy ss = new JavaSerializationStrategy();
        List list = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals(list, ss.deSerialize(ss.serialize(list)));
    }
    
}
