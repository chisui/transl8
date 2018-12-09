package com.github.chisui.translate;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class GetterTest {

    private static final Getter<String, String> TO_UPPER_CASE = String::toUpperCase;

    @Test
    @Parameters({
            "true, java.lang.String",
            "false, java.lang.Object",
            "false, java.lang.Number",
    })
    public void testVerifyArgs(boolean expected, Class<?> cls) {
        assertEquals(expected, TO_UPPER_CASE.acceptsArgumentsOfType(cls));
    }
}