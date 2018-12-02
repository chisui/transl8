package com.github.chisui.translate;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class ComposedFormatterSource<T, R> implements FormatterSource<R> {

    private final KeyToString keyToString;
    private final BiFunction<? super String, ? super Locale, ? extends Optional<T>> messageSource;
    private final BiFunction<? super T, ? super Locale, ? extends Formatter<Object, R>> toFormatter;

    ComposedFormatterSource(
            KeyToString keyToString,
            BiFunction<? super String, ? super Locale, ? extends Optional<T>> messageSource,
            BiFunction<? super T, ? super Locale, ? extends Formatter<Object, R>> toFormatter) {
        this.keyToString = requireNonNull(keyToString, "keyToString");
        this.messageSource = requireNonNull(messageSource, "messageSource");
        this.toFormatter = requireNonNull(toFormatter, "toFormatter");
    }

    @Override
    @SuppressWarnings({
            "unchecked", // Java's type system is not strong enough.
    })
    public <K extends TranslationKey<K, A>, A> Optional<Formatter<A, R>> formatterOf(Locale locale, K key) {
        return (Optional) messageSource.apply(keyToString.toKeyString(key), locale)
                .map(t -> toFormatter.apply(t, locale));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComposedFormatterSource<?, ?> that = (ComposedFormatterSource<?, ?>) o;
        return keyToString.equals(that.keyToString) &&
                messageSource.equals(that.messageSource) &&
                toFormatter.equals(that.toFormatter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyToString, messageSource, toFormatter);
    }

    @Override
    public String toString() {
        return "ComposedFormatterSource{" +
                "keyToString=" + keyToString +
                ", messageSource=" + messageSource +
                ", toFormatter=" + toFormatter +
                '}';
    }
}
