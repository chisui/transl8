package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Formatter} that applies its argument to multiple sub {@link Formatter Formatters} and combines the result
 * using a {@link Collector}.
 *
 * @param <Arg> argument type
 * @param <Return> return type
 */
public final class CollectingFormatter<Arg, Return>
        implements Formatter<Arg, Return> {

    private final Collector<? super Return, ?, Return> collector;
    private final List<Formatter<? super Arg, Return>> subFormatters;

    CollectingFormatter(
            Collector<? super Return, ?, Return> collector,
            List<Formatter<? super Arg, Return>> subFormatters) {
        this.collector = requireNonNull(collector, "CollectingFormatter.collector");
        this.subFormatters = requireNonNull(subFormatters, "CollectingFormatter.subFormatters");
    }

    @Override
    public boolean acceptsArgumentsOfType(Type type) {
        return subFormatters().stream()
                .allMatch(f -> f.acceptsArgumentsOfType(type));
    }

    @Override
    public Return apply(TranslationFunction<Return> translator, Arg arg) {
        return subFormatters().stream()
                .map(f -> f.apply(translator, arg))
                .collect(collector());
    }

    public Collector<? super Return, ?, Return> collector() {
        return collector;
    }

    public List<Formatter<? super Arg, Return>> subFormatters() {
        return subFormatters;
    }
}
