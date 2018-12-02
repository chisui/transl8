package com.github.chisui.translate;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

import static nl.jqno.equalsverifier.Warning.NULL_FIELDS;
import static org.junit.Assert.assertEquals;

public class ComposedFormatterSourceTest {

    KeyToString keyToString = new KeyToString() {
        @Override
        public <SELF extends TranslationKey<SELF, A>, A> String toKeyString(TranslationKey<SELF, A> key) {
            return null;
        }

        @Override
        public String toString() {
            return "keyToString";
        }
    };
    BiFunction<String, Locale, Optional<String>> messageSource = new BiFunction<String, Locale, Optional<String>>() {
        @Override
        public Optional<String> apply(String s, Locale locale) {
            return Optional.empty();
        }

        @Override
        public String toString() {
            return "messageSource";
        }
    };
    BiFunction<String, Locale, Formatter<Object, String>> toFormatter = new BiFunction<String, Locale, Formatter<Object, String>>() {
        @Override
        public Formatter<Object, String> apply(String s, Locale locale) {
            return null;
        }

        @Override
        public String toString() {
            return "toFormatter";
        }
    };

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(ComposedFormatterSource.class)
                .usingGetClass()
                .suppress(NULL_FIELDS)
                .verify();
    }

    @Test
    public void testToString() {
        assertEquals("ComposedFormatterSource{keyToString=keyToString, messageSource=messageSource, toFormatter=toFormatter}",
                FormatterSource.of(keyToString, messageSource, toFormatter).toString());
    }

    @Test
    public void testOfDefaultKeyToString() {
        assertEquals("ComposedFormatterSource{keyToString=DefaultKeyToString, messageSource=messageSource, toFormatter=toFormatter}",
                FormatterSource.of(messageSource, toFormatter).toString());
    }
}