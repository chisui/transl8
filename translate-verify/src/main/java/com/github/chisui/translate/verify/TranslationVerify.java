package com.github.chisui.translate.verify;

import com.github.chisui.translate.*;
import com.github.chisui.translate.Formatter;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.Tables;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

import static com.github.chisui.translate.ObjectUtils.toClass;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

public final class TranslationVerify {
    private TranslationVerify() {}



    public static <R> Stream<Cell<Locale, TranslationKey<?, ?>, Optional<Formatter<?, R>>>> allFormatters(
            String basePackage,
            FormatterSource<R> formatterSource,
            Locale... supportedLocales) {
        return discoverTranslationKeysInternal(basePackage)
                .flatMap(key -> Arrays.stream(supportedLocales)
                    .map(locale -> Tables.immutableCell(locale, key, formatterSource.formatterOf(locale, (TranslationKey) key))));
    }

    public static Set<TranslationKey<?, ?>> discoverTranslationKeys(String basePackage) {
        return discoverTranslationKeysInternal(basePackage)
                .collect(toImmutableSet());
    }

    private static Stream<TranslationKey<?, ?>> discoverTranslationKeysInternal(String basePackage) {
        Reflections refl = new Reflections(basePackage);

        return Stream.concat(
                translatableTranslationKeys(refl),
                enumTranslationKeys(refl)
        );
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
}
