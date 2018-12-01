package com.github.chisui.translate;

import org.junit.Test;

import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class DefaultKeyToStringTest {

    private static final class BrokenTranslationKey implements TranslationKey<BrokenTranslationKey, Long> {
        @Override public Type argType() { return null; }
        @Override public String toKeyString() { return null; }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultToKeyStringMalformedKey() {
        DefaultKeyToString.defaultToKeyString(new BrokenTranslationKey());
    }
}