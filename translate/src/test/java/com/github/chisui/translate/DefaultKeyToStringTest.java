package com.github.chisui.translate;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class DefaultKeyToStringTest {

    private static final class BrokenTranslationKey implements TranslationKey<BrokenTranslationKey, Long> {
        @Override
        public Type argType() {
            return null;
        }

        @Override
        public String toKeyString() {
            return null;
        }
    }

    @Test
    public void testDefaultToKeyString() {
        assertEquals(SimpleExampleTranslatable.class.getCanonicalName(),
                DefaultKeyToString.defaultToKeyString(TranslationKey.of(SimpleExampleTranslatable.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultToKeyStringMalformedKey() {
        DefaultKeyToString.defaultToKeyString(new BrokenTranslationKey());
    }

    private enum BrokenEnum implements EnumTranslationKey<BrokenEnum, Void> {
        BROKEN;

        static {
            try {
                // intentionally breaking the name part of the Enum constant to simulate broken bytecode.
                Field nameField = Enum.class.getDeclaredField("name");
                nameField.setAccessible(true);
                // BrokenEnum.BROKEN is now basically unusable forever
                nameField.set(BrokenEnum.BROKEN, "reallyBroken");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new AssertionError("For Tests to work we need to be able to break this Enum", e);
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testDefaultToKeyStringBrokenEnum() {
        DefaultKeyToString.defaultToKeyString(BrokenEnum.BROKEN);
    }
}