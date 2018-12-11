package com.github.chisui.translate.spring.expression;

import static org.junit.Assert.assertEquals;

import com.github.chisui.translate.*;
import org.junit.Test;

import java.util.Locale;

public class TranslateSpelIT {

    private static final FormatterSourceTranslator<String> TRANSLATOR = Translator.of(
            ResourceBundleMessageSource.of("test"),
            ToSpelFormatter.of());

    @TranslationOverride("spel")
    enum TestKey implements EnumTranslationKey<TestKey, String[]> {
        TEST
    }

    @Test
    public void testIntegrationList() {
        assertEquals("Hello World", TRANSLATOR.apply(Locale.ENGLISH, TestKey.TEST, "Hello", "World"));
    }

    @Test
    public void testIntegrationObject() {
        assertEquals("Hello World", TRANSLATOR.apply(Locale.ENGLISH, new TestPojo("World")));
    }
}
