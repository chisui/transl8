package com.github.chisui.translate;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

/**
 * Composes {@link KeyToString} with a message source and a {@link Formatter} constructor into a
 * {@link FormatterSource}.
 *
 * @param <Msg> type of message to format
 * @param <Return> return value of the {@link Formatter}
 */
public final class ComposedFormatterSource<Msg, Return>
        implements FormatterSource<Return> {

    private final KeyToString keyToString;
    private final BiFunction<
            ? super String, ? super Locale,
            ? extends Optional<Msg>> messageSource;
    private final BiFunction<
            ? super Msg, ? super Locale,
            ? extends Formatter<Object, Return>> toFormatter;

    ComposedFormatterSource(
            final KeyToString keyToString,
            final BiFunction<
                    ? super String, ? super Locale,
                    ? extends Optional<Msg>> messageSource,
            final BiFunction<
                    ? super Msg, ? super Locale,
                    ? extends Formatter<Object, Return>> toFormatter) {
        this.keyToString = requireNonNull(keyToString, "keyToString");
        this.messageSource = requireNonNull(messageSource, "messageSource");
        this.toFormatter = requireNonNull(toFormatter, "toFormatter");
    }

    @Override
    @SuppressWarnings({
            "unchecked", // Java's type system is not strong enough.
    })
    public <Key extends TranslationKey<Key, Arg>, Arg>
            Optional<Formatter<Arg, Return>> formatterOf(
                    final Locale locale,
                    final Key key) {
        return (Optional) messageSource.apply(keyToString.toKeyString(key), locale)
                .map(msg -> toFormatter.apply(msg, locale));
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
