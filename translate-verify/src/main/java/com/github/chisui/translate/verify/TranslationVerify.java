package com.github.chisui.translate.verify;

import com.github.chisui.translate.*;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

public final class TranslationVerify {
    private TranslationVerify() {}

    public static Set<TranslationKey<?, ?>> discoverTranslationKeys(String basePackage) {
        Reflections refl = new Reflections(basePackage);

        return Stream.concat(
                translatableTranslationKeys(refl),
                enumTranslationKeys(refl)
        ).collect(toImmutableSet());
    }

    @SuppressWarnings({
            "unchecked"
    })
    private static Stream<EnumTranslationKey<?, ?>> enumTranslationKeys(Reflections refl) {
        return refl.getSubTypesOf(EnumTranslationKey.class)
            .stream()
            .flatMap(TranslationVerify::enumValues)
            .map(EnumTranslationKey.class::cast);
    }

    private static Stream<? extends ClassTranslationKey<? extends Translatable>> translatableTranslationKeys(Reflections refl) {
        return refl.getSubTypesOf(Translatable.class)
            .stream()
            .map(TranslationKey::of);
    }

    @SuppressWarnings({
            "unchecked" // is checked through contract of Enum
    })
    private static Stream<Enum<?>> enumValues(Type enumType) {
        try {
            Enum<?>[] values = (Enum[]) toClass(enumType)
                    .getMethod("values")
                    .invoke(null);
            return Arrays.stream(values);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Class<?> toClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return toClass(((ParameterizedType) type).getRawType());
        } else {
            throw new IllegalStateException("could not determine class from " + type);
        }
    }
}
