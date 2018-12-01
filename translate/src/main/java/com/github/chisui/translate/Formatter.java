package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.Locale;

@FunctionalInterface
public interface Formatter<A, R> {

    default boolean acceptsArgumentsOfType(Type type) {
        return false;
    }

    R apply(A arg, Locale locale, TranslationFunction<R> translator);

}
