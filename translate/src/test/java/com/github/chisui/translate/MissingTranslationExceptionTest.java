package com.github.chisui.translate;

import org.junit.Test;

import java.util.Locale;

import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumIndirect.SOME_CONST;
import static org.junit.Assert.*;

public class MissingTranslationExceptionTest {

    @Test
    public void testGetMessage() {
        assertEquals("Could not find translation for en:super.TestEnumIndirect.some_const in source",
                new MissingTranslationException("source", Locale.ENGLISH, SOME_CONST).getMessage());
    }
}