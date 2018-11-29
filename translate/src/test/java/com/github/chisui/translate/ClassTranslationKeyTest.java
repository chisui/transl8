package com.github.chisui.translate;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static nl.jqno.equalsverifier.Warning.NULL_FIELDS;


public class ClassTranslationKeyTest {

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(ClassTranslationKey.class)
                .usingGetClass()
                .suppress(NULL_FIELDS)
                .verify();
    }
}