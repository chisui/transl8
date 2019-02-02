package com.github.chisui.translate;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.*;

public interface Formatter<Arg, Return>
        extends
            BiFunction<TranslationFunction<Return> , Arg, Return>,
            VerifyArguments {

    static <T, R> Collector<Formatter<? super T, R>, ?, CollectingFormatter<T, R>> toFormatter(
            Collector<? super R, ?, R> collector) {
        return collectingAndThen(toList(), fs -> new CollectingFormatter<>(collector, fs));
    }

    @SafeVarargs
    static <T> CollectingFormatter<T, String> ofJoining(Formatter<T, String>... subFormatters) {
        return of(joining(), subFormatters);
    }

    @SafeVarargs
    static <T, R> CollectingFormatter<T, R> of(
            Collector<? super R, ?, R> collector,
            Formatter<? super T, R>... subFormatters) {
        return Arrays.stream(subFormatters)
                .collect(toFormatter(collector));
    }

    @SuppressWarnings({
            "unchecked", "rawtypes" // java type inference sucks hard
    })
    static <A, B, R> ComposedFormatter<A, B, R> byKey(Getter<A, B> getter, TranslationKey<?, ? super B> key) {
        requireNonNull(key, "ComposedFormatter.key");
        return by(getter, (t, b) -> (R) t.apply((TranslationKey) key, b));
    }

    static <A, B extends Translatable, R> ComposedFormatter<A, B, R> byTranslatable(Getter<A, B> getter) {
        return by(getter, (t, b) -> t.apply(b));
    }

    static <A, B, R> ComposedFormatter<A, B, R> by(Getter<A, B> getter, Function<B, R> f) {
        requireNonNull(f, "ComposedFormatter.f");
        return by(getter, (t, b) -> f.apply(b));
    }

    static <A, B, R> ComposedFormatter<A, B, R> by(Getter<A, B> getter, BiFunction<TranslationFunction<R>, B, R> f) {
        return new ComposedFormatter<>(getter, f);
    }

    static <T, R> ConstFormatter<T, R> of(R value) {
        return new ConstFormatter<>(value);
    }

}
