package com.github.chisui.translate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class EnumTranslationKeyTest {

    @Test
    public void testEnumImpl() {
        assertEquals("com.github.chisui.translate.TestEnumKey.hello",
                TestEnumKey.HELLO.toKeyString());
    }

    @Test
    public void testEnumCompositionImple() {
        assertEquals("com.github.chisui.translate.TestEnumKey.goodbye.earth",
                TestEnumCompositionKey.EARTH.compose(TestEnumKey.GOODBYE).toKeyString());
    }

}
