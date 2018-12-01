package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.function.BiFunction;

public interface Formatter<A, R> extends BiFunction<A, TranslationFunction<R>, R> {

    default boolean acceptsArgumentsOfType(Type type) {
        return false;
    }

}
