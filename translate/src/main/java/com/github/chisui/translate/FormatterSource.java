package com.github.chisui.translate;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

public interface FormatterSource<R> {

    <K extends TranslationKey<K, A>, A> Optional<Formatter<A, R>> formatterOf(Locale locale, K key);

    static <T, R> ComposedFormatterSource<T, R> of(
            KeyToString keyToString,
            BiFunction<? super String, ? super Locale, Optional<T>> messageSource,
            BiFunction<? super T, ? super Locale, ? extends Formatter<Object, R>> toFormatter) {
        return new ComposedFormatterSource<>(keyToString, messageSource, toFormatter);
    }

    static <T, R> ComposedFormatterSource<T, R> of(
            BiFunction<? super String, ? super Locale, Optional<T>> messageSource,
            BiFunction<? super T, ? super Locale, ? extends Formatter<Object, R>> toFormatter) {
        return of(DefaultKeyToString.instance(), messageSource, toFormatter);
    }

}
