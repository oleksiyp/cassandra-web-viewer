package org.cassandra_viewer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * User: captain-protect
 * Date: 4/5/12
 * Time: 1:08 PM
 */
public class CassandraDeserializer {

    enum Type {
        NULL,
        BOOLEAN,
        CHAR,
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        BYTES,
        DATE,
        STRING,
        UUID,
        OBJECT,
    }
    private Map<Byte, Type> typeMap = new HashMap();

    public CassandraDeserializer(String definition) {
        String[] defs = definition.split("\\s*,\\s*");
        for (String def : defs) {
            String[] values = def.split("->");
            String codeStr = values[0];
            String type = values[1];
            int code = Integer.parseInt(codeStr);

            typeMap.put((byte) code, Type.valueOf(type.toUpperCase()));
        }
    }

    public Object deserialize(byte []bytes) {
        Type type = typeMap.get(bytes[0]);
        if (type == null) {
            return new ByteArrayWrapper(decodeBytes(bytes, 0));
        }
        switch (type) {
            case NULL: return null;
            case BOOLEAN: return bytes[1] == 1;
            case CHAR: return (int) decodeLong(bytes, 3);
            case SHORT: return (int) decodeLong(bytes, 3);
            case INTEGER: return (int) decodeLong(bytes, 3);
            case LONG: return decodeLong(bytes, 3);
            case BYTES: return new ByteArrayWrapper(decodeBytes(bytes, 1));
            case FLOAT: return Float.intBitsToFloat((int) decodeLong(bytes, 3));
            case DOUBLE: return Double.longBitsToDouble((int) decodeLong(bytes, 3));
            case DATE: return new Date(decodeLong(bytes, 3));
            case STRING: return decodeString(bytes);
            case UUID: return decodeUUID(bytes);
            case OBJECT: return decodeObject(bytes);
            default: return new ByteArrayWrapper(decodeBytes(bytes, 0));
        }
    }

    private Object decodeObject(byte[] bytes) {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes, 1, bytes.length - 1);
        try {
            ObjectInputStream in = new ObjectInputStream(bin);
            return in.readObject();
        } catch (Exception ex) {
            return null;
        }
    }

    private Object decodeUUID(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes, 0, 17);
        bb.get();
        return new UUID(bb.getLong(), bb.getLong());
    }

    private Object decodeString(byte[] bytes) {
        try {
            return new String(decodeBytes(bytes, 1), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "<BAD ENCODING>";
        }
    }

    private byte[] decodeBytes(byte[] bytes, int offset) {
        byte []result = new byte[bytes.length - offset];
        System.arraycopy(bytes, offset, result, 0, result.length);
        return result;
    }

    private long decodeLong(byte[] bytes, int shift) {
        long result = 0;
        for (int i = 1; i < bytes.length; i++) {
            result |=  ((long) bytes[i] & 0xFF) << (bytes.length - i << shift);
        }
        return result;
    }

    public static class ByteArrayWrapper implements Comparable<ByteArrayWrapper> {
        public byte[] array;

        public ByteArrayWrapper(byte[] array) {
            this.array = array;
        }

        public byte[] getArray() {
            return array;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            boolean first = true;
            for (byte bt : array) {
                if (!first) {
                    builder.append(",");
                }
                first = false;
                builder.append(String.format("%02X", bt));
            }
            builder.append("]");
            return builder.toString();
        }

        @Override
        public int compareTo(ByteArrayWrapper o) {
            if (array.length < o.array.length) {
                return -1;
            }
            if (array.length > o.array.length) {
                return 1;
            }
            for (int i = 0; i < array.length; i++) {
                if (array[i] < o.array[i]) {
                    return -1;
                }
                if (array[i] > o.array[i]) {
                    return 1;
                }
            }
            return 0;
        }
    }
}
