package com.github.chisui.translate;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

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
                ret.add(new Object[] {
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
                {null, "null"},
                {"a", "a"},
                {"b", "b"},
                {new long[]{1}, "[1]"},
                {new long[]{2}, "[2]"},
                {new int[]{1}, "[1]"},
                {new int[]{2}, "[2]"},
                {new short[]{1}, "[1]"},
                {new short[]{2}, "[2]"},
                {new char[]{'a'}, "[a]"},
                {new char[]{'b'}, "[b]"},
                {new byte[]{1}, "[1]"},
                {new byte[]{2}, "[2]"},
                {new boolean[]{true}, "[true]"},
                {new boolean[]{false}, "[false]"},
                {new double[]{1.0}, "[1.0]"},
                {new double[]{2.0}, "[2.0]"},
                {new float[]{1.0f}, "[1.0]"},
                {new float[]{2.0f}, "[2.0]"},
                {new Object[]{BigDecimal.TEN}, "[10]"},
                {new Object[]{BigDecimal.ONE}, "[1]"},
                {new String[]{"a"}, "[a]"},
                {new String[]{"b"}, "[b]"},
        };
    }

    @Test
    @Parameters(method = "values")
    public void testEquals(boolean bool, Object a, Object b) {
        assertEquals(bool, ObjectUtils.equals(a, b));
    }

    @Test
    @Parameters(method = "vals")
    public void testToString(Object obj, String str) {
        assertEquals(str, ObjectUtils.toString(obj));
    }
}