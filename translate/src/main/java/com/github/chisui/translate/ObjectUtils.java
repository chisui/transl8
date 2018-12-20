package com.github.chisui.translate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Utility class to deal with objects in a generic way.
 */
public final class ObjectUtils {
    private ObjectUtils() {
    }

    /**
     * Generic equals method.
     * If both objects are arrays of the same type the corresponding equals method in {@link Arrays} is used.
     * There is no value coercion done. Array types have to exactly match. The only exception to this are
     * {@link Object[]}. Each non primitive array is treated as {@link Object[]} and are compared using
     * {@link Arrays#equals(Object[], Object[])}.
     * Otherwise the first objects equals method is used.
     *
     * @param a first object
     * @param b second object
     * @return whether or not both objects are equal.
     */
    public static boolean equals(Object a, Object b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null) {
            return false;
        } else if (a.getClass().isArray() && b.getClass().isArray()) {
            if (a instanceof long[] && b instanceof long[]) {
                return Arrays.equals((long[]) a, (long[]) b);
            } else if (a instanceof int[] && b instanceof int[]) {
                return Arrays.equals((int[]) a, (int[]) b);
            } else if (a instanceof short[] && b instanceof short[]) {
                return Arrays.equals((short[]) a, (short[]) b);
            } else if (a instanceof char[] && b instanceof char[]) {
                return Arrays.equals((char[]) a, (char[]) b);
            } else if (a instanceof byte[] && b instanceof byte[]) {
                return Arrays.equals((byte[]) a, (byte[]) b);
            } else if (a instanceof boolean[] && b instanceof boolean[]) {
                return Arrays.equals((boolean[]) a, (boolean[]) b);
            } else if (a instanceof double[] && b instanceof double[]) {
                return Arrays.equals((double[]) a, (double[]) b);
            } else if (a instanceof float[] && b instanceof float[]) {
                return Arrays.equals((float[]) a, (float[]) b);
            } else if (a instanceof Object[] && b instanceof Object[]) {
                return Arrays.deepEquals((Object[]) a, (Object[]) b);
            } else {
                return false;
            }
        } else {
            return a.equals(b);
        }
    }

    /**
     * Generic toString function that is able to turn arrays to strings.
     * {@code null} is turned into {@code "null"}. Arrays are turned into Strings using the corresponding
     * {@link Arrays#toString(int[])} function. For all other objects the own {@link Object#toString()} method is used.
     *
     * @param obj to turn into {@link String}
     * @return the resulting {@link String}
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof long[]) {
            return Arrays.toString((long[]) obj);
        } else if (obj instanceof int[]) {
            return Arrays.toString((int[]) obj);
        } else if (obj instanceof short[]) {
            return Arrays.toString((short[]) obj);
        } else if (obj instanceof char[]) {
            return Arrays.toString((char[]) obj);
        } else if (obj instanceof byte[]) {
            return Arrays.toString((byte[]) obj);
        } else if (obj instanceof boolean[]) {
            return Arrays.toString((boolean[]) obj);
        } else if (obj instanceof double[]) {
            return Arrays.toString((double[]) obj);
        } else if (obj instanceof float[]) {
            return Arrays.toString((float[]) obj);
        } else if (obj instanceof Object[]) {
            return Arrays.toString((Object[]) obj);
        } else {
            return obj.toString();
        }
    }

    /**
     * Generic Array boxing function.
     * If the object is not an array or {@link Iterable} an {@link Optional#empty()} is returned.
     * If the object is an {@code Object[]} the object itself is returned. If the object is a primitive array it is
     * turned into it's boxed variant. For example {@code int[]} is turned into {@code Integer[]}.
     * Instances of {@link Collection} and {@link Iterable} are turned into an {@code Object[]} by iterating over them.
     *
     * @param obj to turn into an {@code Object[]}
     * @return the {@code Object[]} or empty.
     */
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    public static Optional<Object[]> toArrayUnsafe(Object obj) {
        Object[] objs;
        if (obj instanceof Object[]) {
            return Optional.of((Object[]) obj);
        } else if (obj instanceof Collection) {
            objs = new Object[((Collection) obj).size()];
            int i = 0;
            for (Object o : (Collection<?>) obj) {
                objs[i++] = o;
            }
        } else if (obj instanceof Iterable) {
            return Optional.of(StreamSupport
                    .stream(((Iterable) obj).spliterator(), false)
                    .toArray());
        } else if (obj instanceof long[]) {
            long[] arr = (long[]) obj;
            objs = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                objs[i] = arr[i];
            }
        } else if (obj instanceof int[]) {
            int[] arr = (int[]) obj;
            objs = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                objs[i] = arr[i];
            }
        } else if (obj instanceof short[]) {
            short[] arr = (short[]) obj;
            objs = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                objs[i] = arr[i];
            }
        } else if (obj instanceof char[]) {
            char[] arr = (char[]) obj;
            objs = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                objs[i] = arr[i];
            }
        } else if (obj instanceof byte[]) {
            byte[] arr = (byte[]) obj;
            objs = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                objs[i] = arr[i];
            }
        } else if (obj instanceof boolean[]) {
            boolean[] arr = (boolean[]) obj;
            objs = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                objs[i] = arr[i];
            }
        } else if (obj instanceof double[]) {
            double[] arr = (double[]) obj;
            objs = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                objs[i] = arr[i];
            }
        } else if (obj instanceof float[]) {
            float[] arr = (float[]) obj;
            objs = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                objs[i] = arr[i];
            }
        } else {
            return Optional.empty();
        }
        return Optional.of(objs);
    }

    /**
     * Generic function to determine the underlying {@link Class} of a {@link Type}.
     * If the {@link Type} is a {@link Class} the argument itself is returned. If the {@link Type} is a
     * {@link ParameterizedType} the {@link ParameterizedType#getRawType() rawType} is returned.
     * Otherwise an {@link IllegalArgumentException} is thrown
     *
     * @param t to determine {@link Class} of
     * @return the {@link Class}
     * @throws IllegalArgumentException if the {@link Class} can't be determined
     */
    public static Class<?> toClass(Type t) {
        if (t instanceof Class) {
            return (Class<?>) t;
        } else if (t instanceof ParameterizedType) {
            return toClass(((ParameterizedType) t).getRawType());
        } else {
            throw new IllegalArgumentException("can not turn " + t + " into a class");
        }
    }
}
