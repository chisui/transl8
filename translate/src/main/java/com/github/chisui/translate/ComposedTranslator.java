package com.github.chisui.translate;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ComposedTranslator<T, R> implements Translator<R> {
    private final Function<? super T, ? extends Formatter<Object, R>> toFormatter;
    private final BiFunction<? super Locale, ? super String, ? extends T> messageSource;
    private final KeyToString keyToString;

    ComposedTranslator(
            KeyToString keyToString,
            BiFunction<? super Locale, ? super String, ? extends T> messageSource,
            Function<? super T, ? extends Formatter<Object, R>> toFormatter) {
        this.keyToString = requireNonNull(keyToString, "keyToString");
        this.messageSource = requireNonNull(messageSource, "messageSource");
        this.toFormatter = requireNonNull(toFormatter, "toFormatter");
    }

    @Override
    public <K extends TranslationKey<K, A>, A> R apply(Locale locale, K key, A arg) {
        return toFormatter.apply(messageSource.apply(locale, keyToString.toKeyString(key)))
                .apply(arg, locale, this.bindLocale(locale));
    }
}
