package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Formatter} composed with another function.
 *
 * @param <Arg> argument type of the resulting {@link Formatter}
 * @param <B> intermediate type
 * @param <Return> return type of the resulting {@link Formatter}
 */
public final class ComposedFormatter<Arg, B, Return>
        implements Formatter<Arg, Return> {

    private final Getter<Arg, B> getter;
    private final BiFunction<TranslationFunction<Return>, B, Return> f;

    ComposedFormatter(
            final Getter<Arg, B> getter,
            final BiFunction<TranslationFunction<Return>, B, Return> f) {
        this.getter = requireNonNull(getter, "ComposedFormatter.getter");
        this.f = requireNonNull(f, "ComposedFormatter.f");
    }

    @Override
    public boolean acceptsArgumentsOfType(final Type type) {
        return getter.acceptsArgumentsOfType(type);
    }

    @Override
    public Return apply(
            final TranslationFunction<Return> translator,
            final Arg arg) {
        return f.apply(translator, getter.apply(arg));
    }
}
