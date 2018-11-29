package com.github.chisui.translate;

import java.util.Arrays;

public final class ObjectUtils {
    private ObjectUtils() {}

    public static boolean equals(Object a, Object b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null) {
            return false;
        } else if (a.getClass().isArray() && b.getClass().isArray()) {
            if(a instanceof long[] && b instanceof long[]) {
                return Arrays.equals((long[]) a, (long[]) b);
            } else if(a instanceof int[] && b instanceof int[]) {
                return Arrays.equals((int[]) a, (int[]) b);
            } else if(a instanceof short[] && b instanceof short[]) {
                return Arrays.equals((short[]) a, (short[]) b);
            } else if(a instanceof char[] && b instanceof char[]) {
                return Arrays.equals((char[]) a, (char[]) b);
            } else if(a instanceof byte[] && b instanceof byte[]) {
                return Arrays.equals((byte[]) a, (byte[]) b);
            } else if(a instanceof boolean[] && b instanceof boolean[]) {
                return Arrays.equals((boolean[]) a, (boolean[]) b);
            } else if(a instanceof double[] && b instanceof double[]) {
                return Arrays.equals((double[]) a, (double[]) b);
            } else if(a instanceof float[] && b instanceof float[]) {
                return Arrays.equals((float[]) a, (float[]) b);
            } else if(a instanceof Object[] && b instanceof Object[]) {
                return Arrays.deepEquals((Object[]) a, (Object[]) b);
            } else {
                return false;
            }
        } else {
            return a.equals(b);
        }
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        } else if(obj instanceof long[]) {
            return Arrays.toString((long[]) obj);
        } else if(obj instanceof int[]) {
            return Arrays.toString((int[]) obj);
        } else if(obj instanceof short[]) {
            return Arrays.toString((short[]) obj);
        } else if(obj instanceof char[]) {
            return Arrays.toString((char[]) obj);
        } else if(obj instanceof byte[]) {
            return Arrays.toString((byte[]) obj);
        } else if(obj instanceof boolean[]) {
            return Arrays.toString((boolean[]) obj);
        } else if(obj instanceof double[]) {
            return Arrays.toString((double[]) obj);
        } else if(obj instanceof float[]) {
            return Arrays.toString((float[]) obj);
        } else if(obj instanceof Object[]) {
            return Arrays.toString((Object[]) obj);
        } else {
            return obj.toString();
        }
    }
}
