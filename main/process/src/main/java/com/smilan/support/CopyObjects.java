package com.smilan.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Thomas
 * @deprecated poor performanace
 *
 */
@Deprecated
public final class CopyObjects {

    /** Private default constructor: cannot be instantiated */
    private CopyObjects() {
    }

    /**
     * Deep copy the object with java serialization 80 000ns poor performanace
     *
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(T object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
            oos.flush();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            return (T) new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
