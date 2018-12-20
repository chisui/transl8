package com.github.chisui.translate.spring.expression;

import com.github.chisui.translate.Translatable;
import com.github.chisui.translate.TranslationFunction;
import org.junit.Test;

import java.util.Locale;

import static com.github.chisui.translate.TranslationFunction.ofUnsafe;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ToSpelFormatterTest {

    public static class TestTranslatable implements Translatable {}

    private static final TranslationFunction<String> TRANSLATE = ofUnsafe((k, a) -> {
        assertTrue(a instanceof TestTranslatable);
        return "x";
    });

    @Test
    public void testSpelFormatter() {
        SpelFormatter<Object> formatter = ToSpelFormatter.of()
                .apply("Hello #{a}", Locale.ENGLISH);
        assertEquals("Hello World",
                    formatter.apply(TRANSLATE, new TestPojo("World")));
    }

    @Test
    public void testSpelFormatterTranslate() {
        SpelFormatter<Object> formatter = ToSpelFormatter.of()
                .apply("Hello #{translate(new com.github.chisui.translate.spring.expression.ToSpelFormatterTest.TestTranslatable())}", Locale.ENGLISH);
        assertEquals("Hello x",
                formatter.apply(TRANSLATE, new TestPojo("World")));
    }

}