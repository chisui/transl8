package com.github.chisui.translate;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A {@link FormatterSource} is a typesafe lookup that looks up a {@link Formatter} for a given {@link TranslationKey}
 * and {@link Locale}.
 *
 * @param <Return> return type of retrieved {@link Formatter Formatters}
 */
public interface FormatterSource<Return> {

    /**
     * Looks up a {@link Formatter} for a given {@link TranslationKey} and {@link Locale}.
     *
     * @param locale to look up with
     * @param key to look up with
     * @param <Key> self referential type of {@link TranslationKey}
     * @param <Arg> argument type the {@link Formatter} associated with the provided key expects
     * @return the {@link Formatter} if present
     */
    <Key extends TranslationKey<Key, Arg>, Arg>
    Optional<Formatter<Arg, Return>> formatterOf(
            final Locale locale,
            final Key key);

    /**
     * Create a {@link FormatterSource} from a message source, a formatter constructor and a way to turn a
     * {@link TranslationKey} into a string key.
     *
     * @param keyToString to turn keys into strings with
     * @param messageSource to lookup messages with
     * @param toFormatter to create formatters from messages with
     * @param <Msg> message type looked up with the message source and input type of formatter constructor
     * @param <Return> the return type of the formatter
     * @return the {@link FormatterSource}
     */
    static <Msg, Return> ComposedFormatterSource<Msg, Return> of(
            final KeyToString keyToString,
            final BiFunction<
                    ? super String, ? super Locale,
                    Optional<Msg>> messageSource,
            final BiFunction<
                    ? super Msg, ? super Locale,
                    ? extends Formatter<Object, Return>> toFormatter) {
        return new ComposedFormatterSource<>(keyToString, messageSource, toFormatter);
    }

    /**
     * Create a {@link FormatterSource} from a message source and a formatter constructor.
     *
     * @param messageSource to lookup messages with
     * @param toFormatter to create formatters from messages with
     * @param <Msg> message type looked up with the message source and input type of formatter constructor
     * @param <Return> the return type of the formatter
     * @return the {@link FormatterSource}
     */
    static <Msg, Return> ComposedFormatterSource<Msg, Return> of(
            final BiFunction<
                    ? super String, ? super Locale,
                    Optional<Msg>> messageSource,
            final BiFunction<
                    ? super Msg, ? super Locale,
                    ? extends Formatter<Object, Return>> toFormatter) {
        return of(DefaultKeyToString.instance(), messageSource, toFormatter);
    }

}
