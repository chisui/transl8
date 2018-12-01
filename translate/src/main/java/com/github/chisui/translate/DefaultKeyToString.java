package com.github.chisui.translate;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.Collections.singletonList;

public final class DefaultKeyToString implements KeyToString {


    @Override
    public <SELF extends TranslationKey<SELF, A>, A> String toKeyString(TranslationKey<SELF, A> key) {
        return DefaultKeyToString.defaultToKeyString(key);
    }

    public static <SELF extends TranslationKey<SELF, A>, A> String defaultToKeyString(TranslationKey<SELF, A> key) {
        String baseStr = toKeyStringBase(key);
        if (key instanceof Enum && key instanceof EnumTranslationKey) {
            Field enumConstantField = enumConstantField((Enum<?>) key);
            String enumStr = Optional.ofNullable(enumConstantField.getDeclaredAnnotation(TranslationOverride.class))
                    .map(TranslationOverride::value)
                    .orElseGet(((Enum) key)::name);
            return baseStr + "." + enumStr;
        } else if (key instanceof ClassTranslationKey) {
            return baseStr;
        } else {
            throw new IllegalArgumentException("can not determine key string of " + key);
        }
    }

    private static Field enumConstantField(Enum<?> constant) {
        try {
            return constant.getClass().getField(constant.name());
        } catch (NoSuchFieldException e) {
            // Can only occur if reflection is diabled or maybe if the bytecode was edited?
            throw new IllegalStateException(
                    "expected enum constant but got " + constant + " of type " + constant.getClass(), e);
        }
    }

    private static <A, SELF extends TranslationKey<SELF, A>> String toKeyStringBase(TranslationKey<SELF, A> key) {
        Class<?> cls = cls(key);
        return translationOverride(cls)
                .map(override -> toKeyStringBase(cls, override))
                .orElseGet(cls::getCanonicalName);
    }

    private static Optional<TranslationOverride> translationOverride(Class<?> cls) {
        Queue<Class<?>> clss = new LinkedList<>(singletonList(cls));
        while (!clss.isEmpty()) {
            Class<?> c = clss.poll();
            if (c == Object.class || c == null) {
                break;
            }
            TranslationOverride override = c.getDeclaredAnnotation(TranslationOverride.class);
            if (override != null) {
                return Optional.of(override);
            } else {
                Arrays.stream(c.getInterfaces())
                        .forEach(clss::offer);
            }
        }
        return Optional.empty();
    }

    private static String toKeyStringBase(Class<?> cls, TranslationOverride override) {
        return Optional.of(override.value())
                .filter(DefaultKeyToString::isNotBlank)
                .map(Optional::of)
                .orElseGet(() -> Optional.of(override.prefix())
                        .map(prefix -> prefix + "." + cls.getSimpleName()))
                .orElseGet(cls::getCanonicalName);
    }

    private static boolean isNotBlank(String s) {
        return !s.trim().isEmpty();
    }

    private static <SELF extends TranslationKey<SELF, A>, A> Class<?> cls(TranslationKey<SELF, A> key) {
        if (key instanceof ClassTranslationKey) {
            return ((ClassTranslationKey<?>) key).cls();
        } else if (key instanceof EnumTranslationKey && key instanceof Enum) {
            return key.getClass();
        } else {
            throw new IllegalArgumentException("can not determine translation key class of " + key);
        }
    }
}
