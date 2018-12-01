package com.github.chisui.translate;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumKey.HELLO;
import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumWithOverride.SOME_CONST;
import static org.junit.Assert.*;

public class TranslationRequestTest {

    @Test
    public void testToStringVoid() {
        assertEquals("TranslationRequest{key=enum.some_const}", TranslationRequest.of(SOME_CONST).toString());
    }

    @Test
    public void testToString() {
        assertEquals("TranslationRequest{key=com.github.chisui.translate.EnumTranslationKeyTest.TestEnumKey.hello, arg=[a, b]}",
                TranslationRequest.of(HELLO, new String[]{ "a", "b"}).toString());
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(TranslationRequest.class)
                .usingGetClass()
                .verify();
    }
}