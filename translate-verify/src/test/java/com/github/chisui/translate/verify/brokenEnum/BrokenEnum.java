package com.github.chisui.translate.verify.brokenEnum;

import com.github.chisui.translate.EnumTranslationKey;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This Enum is purposefully broken.
 * Its internal state is corrupted in a way that the {@link #values()} method throw a
 * {@link NullPointerException}.
 */
public enum BrokenEnum implements EnumTranslationKey<BrokenEnum, String> {
    DO_NOT_USE;

    static {
        try {
            Field values = BrokenEnum.class.getDeclaredField("$VALUES");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(values, values.getModifiers() & ~Modifier.FINAL);

            values.setAccessible(true);
            values.set(null, null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new AssertionError("Reflection has to be allowed", e);
        }
    }

}
