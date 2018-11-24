package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.function.Function;

public interface Formatter<A, R> extends Function<A, R> {

    default boolean acceptsArgumentsOfType(Type type) {
        return false;
    }

}
