package com.github.chisui.translate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumTranslationKeyTest {

    @Test
    public void testEnumImpl() {
        assertEquals("com.github.chisui.translate.TestEnumKey.HELLO", TestEnumKey.HELLO.toKeyString());
    }

}
