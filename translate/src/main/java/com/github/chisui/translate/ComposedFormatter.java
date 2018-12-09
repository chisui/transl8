package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public final class ComposedFormatter<A, B, R> implements Formatter<A, R> {

    private final Getter<A, B> getter;
    private final BiFunction<TranslationFunction<R>, B, R> f;

    ComposedFormatter(Getter<A, B> getter, BiFunction<TranslationFunction<R>, B, R> f) {
        this.getter = requireNonNull(getter, "ComposedFormatter.getter");
        this.f = requireNonNull(f, "ComposedFormatter.f");
    }

    @Override
    public boolean acceptsArgumentsOfType(Type type) {
        return getter.acceptsArgumentsOfType(type);
    }

    @Override
    public R apply(TranslationFunction<R> translator, A arg) {
        return f.apply(translator, getter.apply(arg));
    }
}
