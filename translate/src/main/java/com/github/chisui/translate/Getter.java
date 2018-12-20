package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.function.Function;

import static com.github.chisui.translate.ObjectUtils.toClass;
import static net.jodah.typetools.TypeResolver.resolveRawArguments;

@FunctionalInterface
public interface Getter<A, B> extends Function<A, B>, VerifyArguments {

    default boolean acceptsArgumentsOfType(Type type) {
        return resolveRawArguments(Function.class, getClass())[0].isAssignableFrom(toClass(type));
    }

}
