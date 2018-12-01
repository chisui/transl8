package com.github.chisui.translate;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static nl.jqno.equalsverifier.Warning.NULL_FIELDS;
import static org.junit.Assert.assertEquals;


public class ClassTranslationKeyTest {

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(ClassTranslationKey.class)
                .usingGetClass()
                .suppress(NULL_FIELDS)
                .verify();
    }

    @Test
    public void testToKeyString() {
        assertEquals("some.translatable",
                TranslationKey.of(ExampleTranslatable.class).toKeyString());
    }

    @Test
    public void testToString() {
        assertEquals("ClassTranslationKey(" + ExampleTranslatable.class.getCanonicalName() + ":some.translatable" + ')',
                TranslationKey.of(ExampleTranslatable.class).toString());
    }
}