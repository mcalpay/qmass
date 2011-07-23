package org.mca.qmass.core.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: malpay
 * Date: 20.Tem.2011
 * Time: 15:48:30
 */
public interface SerializationStrategy {

    Object deSerialize(byte[] bytes);

    byte[] serialize(Object obj);

}
