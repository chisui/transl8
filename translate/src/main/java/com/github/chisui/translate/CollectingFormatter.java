package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;

public final class CollectingFormatter<T, R> implements Formatter<T, R> {

    private final Collector<? super R, ?, R> collector;
    private final List<Formatter<? super T, R>> subFormatters;

    public CollectingFormatter(
            Collector<? super R, ?, R> collector,
            List<Formatter<? super T, R>> subFormatters) {
        this.collector = requireNonNull(collector, "CollectingFormatter.collector");
        this.subFormatters = requireNonNull(subFormatters, "CollectingFormatter.subFormatters");
    }

    @Override
    public boolean acceptsArgumentsOfType(Type type) {
        return subFormatters.stream()
                .allMatch(f -> f.acceptsArgumentsOfType(type));
    }

    @Override
    public R apply(TranslationFunction<R> translator, T arg) {
        return subFormatters.stream()
                .map(f -> f.apply(translator, arg))
                .collect(collector);
    }



}
