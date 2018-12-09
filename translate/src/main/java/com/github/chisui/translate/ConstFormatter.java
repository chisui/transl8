package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.Objects;

public final class ConstFormatter<T, R> implements Formatter<T, R> {

    private final R value;

    ConstFormatter(R value) {
        this.value = Objects.requireNonNull(value, "ConstFormatter.value");
    }

    @Override
    public boolean acceptsArgumentsOfType(Type type) {
        return true;
    }

    @Override
    public R apply(TranslationFunction<R> translator, T arg) {
        return value;
    }
}
