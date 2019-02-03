package com.github.chisui.translate;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.*;

/**
 * Arg {@link Formatter} is message that can be formatted given a translator and some additional argument.
 *
 * @param <Arg> argument Type
 * @param <Return> return Type
 */
public interface Formatter<Arg, Return>
        extends
            BiFunction<TranslationFunction<Return> , Arg, Return>,
            VerifyArguments {

    /**
     * Create a {@link Collector} that that turns multiple {@link Formatter Formatters} into a single {@link Formatter}.
     *
     * The resulting {@link Formatter} will apply its argument to each sub {@link Formatter} and combines the results
     * using the provided {@link Collector}.
     *
     * @param collector to combine results with
     * @param <Arg> common upper bound of argument Types
     * @param <Result> common lower bound of result types.
     * @return the {@link Collector}
     */
    static <Arg, Result> Collector<Formatter<? super Arg, Result>, ?, CollectingFormatter<Arg, Result>> toFormatter(
            final Collector<? super Result, ?, Result> collector) {
        return collectingAndThen(toList(), fs -> new CollectingFormatter<>(collector, fs));
    }

    /**
     * Create a {@link CollectingFormatter} that concats the results of each sub {@link Formatter} using.
     *
     * @param subFormatters to combine
     * @param <Arg> argument type
     * @return the {@link CollectingFormatter}
     * @see #of(Collector, Formatter[])
     * @see #toFormatter(Collector)
     */
    @SafeVarargs
    static <Arg> CollectingFormatter<Arg, String> ofJoining(
            final Formatter<Arg, String>... subFormatters) {
        return of(joining(), subFormatters);
    }

    /**
     * Create a {@link CollectingFormatter} that applies its argument to each sub {@link Formatter} and combines the
     * results using the provided {@link Collector}.
     *
     * @param collector to use for combing results
     * @param subFormatters to combine
     * @param <Arg> argument type
     * @param <Return> return type
     * @return the {@link CollectingFormatter}
     */
    @SafeVarargs
    static <Arg, Return> CollectingFormatter<Arg, Return> of(
            final Collector<? super Return, ?, Return> collector,
            final Formatter<? super Arg, Return>... subFormatters) {
        return stream(subFormatters)
                .collect(toFormatter(collector));
    }

    /**
     * Create a {@link Formatter} that uses the translation function passed to it to retrieve a specific key and
     * retrieves a value from it.
     *
     * @param getter to apply to lookup result
     * @param key to look up
     * @param <Arg> argument type
     * @param <B> intermediate type
     * @param <Return> return type
     * @return the {@link ComposedFormatter}
     */
    @SuppressWarnings({
            "unchecked", "rawtypes" // java type inference sucks hard
    })
    static <Arg, B, Return> ComposedFormatter<Arg, B, Return> byKey(
            final Getter<Arg, B> getter,
            final TranslationKey<?, ? super B> key) {
        requireNonNull(key, "ComposedFormatter.key");
        return by(getter, (t, b) -> (Return) t.apply((TranslationKey) key, b));
    }

    /**
     * Creates a {@link Formatter} that extracts a {@link Translatable} value from the argument and then passes it to
     * the {@link TranslationFunction}.
     *
     * @param getter to use to extract {@link Translatable}
     * @param <Arg> argument type
     * @param <T> translatable intermediate type
     * @param <Return> return type
     * @return the {@link ComposedFormatter}
     */
    static <Arg, T extends Translatable, Return> ComposedFormatter<Arg, T, Return> byTranslatable(
            final Getter<Arg, T> getter) {
        return by(getter, (t, b) -> t.apply(b));
    }

    /**
     * Creates a {@link Formatter} that extract a value from the argument and passes it to the provided function.
     *
     * @param getter to use to extract value from argument
     * @param f function to apply to retrieved value
     * @param <Arg> argument type
     * @param <B> intermediate type
     * @param <Return> return type
     * @return the {@link ComposedFormatter}
     */
    static <Arg, B, Return> ComposedFormatter<Arg, B, Return> by(
            final Getter<Arg, B> getter,
            final Function<B, Return> f) {
        requireNonNull(f, "ComposedFormatter.f");
        return by(getter, (t, b) -> f.apply(b));
    }

    /**
     * Creates a {@link Formatter} that extract a value from the argument and passes it to the provided function.
     *
     * @param getter to use to extract value from argument
     * @param f function to apply to retrieved value
     * @param <Arg> argument type
     * @param <B> intermediate type
     * @param <Return> return type
     * @return the {@link ComposedFormatter}
     */
    static <Arg, B, Return> ComposedFormatter<Arg, B, Return> by(
            final Getter<Arg, B> getter,
            final BiFunction<TranslationFunction<Return>, B, Return> f) {
        return new ComposedFormatter<>(getter, f);
    }

    /**
     * Creates a {@link Formatter} that always returns the provided value
     *
     * @param value to return.
     * @param <Arg> argument type
     * @param <Return> type of value to return
     * @return the {@link ConstFormatter}
     */
    static <Arg, Return> ConstFormatter<Arg, Return> of(
            final Return value) {
        return new ConstFormatter<>(value);
    }

}
