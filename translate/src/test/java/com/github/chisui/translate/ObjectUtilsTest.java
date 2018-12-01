package com.github.chisui.translate;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class ObjectUtilsTest {

    Object[] values() {
        Object[][] valRow = vals();
        Object[][] valCol = vals();

        List<Object[]> ret = new LinkedList<>();
        for (int row = 0; row < valRow.length; row++) {
            for (int col = 0; col < valCol.length; col++) {
                ret.add(new Object[]{
                        row == col,
                        valRow[row][0],
                        valCol[col][0],
                });
            }
        }
        return ret.toArray();
    }

    Object[][] vals() {
        return new Object[][]{
                {null, "null", null},
                {"a", "a", null},
                {"b", "b", null},
                {new long[]{1}, "[1]", new Object[]{1L}},
                {new long[]{2}, "[2]", new Object[]{2L}},
                {new int[]{1}, "[1]", new Object[]{1}},
                {new int[]{2}, "[2]", new Object[]{2}},
                {new short[]{1}, "[1]", new Object[]{(short) 1}},
                {new short[]{2}, "[2]", new Object[]{(short) 2}},
                {new char[]{'a'}, "[a]", new Object[]{'a'}},
                {new char[]{'b'}, "[b]", new Object[]{'b'}},
                {new byte[]{1}, "[1]", new Object[]{(byte) 1}},
                {new byte[]{2}, "[2]", new Object[]{(byte) 2}},
                {new boolean[]{true}, "[true]", new Object[]{true}},
                {new boolean[]{false}, "[false]", new Object[]{false}},
                {new double[]{1.0}, "[1.0]", new Object[]{1.0}},
                {new double[]{2.0}, "[2.0]", new Object[]{2.0}},
                {new float[]{1.0f}, "[1.0]", new Object[]{(float) 1.0}},
                {new float[]{2.0f}, "[2.0]", new Object[]{(float) 2.0}},
                {new Object[]{BigDecimal.TEN}, "[10]", new Object[]{BigDecimal.TEN}},
                {new Object[]{BigDecimal.ONE}, "[1]", new Object[]{BigDecimal.ONE}},
                {new String[]{"a"}, "[a]", new Object[]{"a"}},
                {new String[]{"b"}, "[b]", new Object[]{"b"}},
        };
    }

    @Test
    @Parameters(method = "values")
    public void testEquals(boolean bool, Object a, Object b) {
        assertEquals(bool, ObjectUtils.equals(a, b));
    }

    @Test
    @Parameters(method = "vals")
    public void testToString(Object obj, String str, Object[] unused) {
        assertEquals(str, ObjectUtils.toString(obj));
    }

    @Test
    @Parameters(method = "vals")
    public void testToArrayUnsafe(Object obj, String unused, Object[] expected) {
        if (expected != null) {
            assertArrayEquals(expected, ObjectUtils.toArrayUnsafe(obj));
        } else {
            try {
                ObjectUtils.toArrayUnsafe(obj);
                fail("expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {

            }
        }
    }

    @Test
    public void testToClassClass() {
        assertEquals(String.class, ObjectUtils.toClass(String.class));
    }

    public List<String> boundGenericType() {
        return null;
    }

    @Test
    public void testToClassGenericType() throws NoSuchMethodException {
        Type type = getClass()
                .getDeclaredMethod("boundGenericType")
                .getGenericReturnType();
        assertEquals(List.class, ObjectUtils.toClass(type));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToClassMalformedType() {
        ObjectUtils.toClass(new Type() {
        });
    }
}