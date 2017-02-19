package com.helospark.sparktemplatingplugin.repository;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteArrayConverterUtil {
    private static final Integer SANITY_CHECK_MAX_INT = 100000;

    public static int readBytes(InputStream is) {
        try {
            byte[] data = new byte[4];
            is.read(data);
            int result = byteArrayToInt(data);
            if (result > SANITY_CHECK_MAX_INT) {
                throw new RuntimeException("Too large number");
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] intToByteArray(int myInteger) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
    }

    public static int byteArrayToInt(byte[] bytes) {
        if (bytes.length != 4) {
            throw new RuntimeException("Not integer");
        }
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
}
