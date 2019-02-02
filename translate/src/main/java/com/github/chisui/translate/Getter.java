package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.function.Function;

import static com.github.chisui.translate.ObjectUtils.toClass;
import static net.jodah.typetools.TypeResolver.resolveRawArguments;

/**
 * Represents an accessor that knows what types may be used as it's argument.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <A> the type of the input to the getter
 * @param <B> the type of the result of the getter
 */
@FunctionalInterface
public interface Getter<A, B> extends Function<A, B>, VerifyArguments {

    /**
     * Checks whether or not this functor object can accept arguments of the provided type.
     * This default implementation retrieves the actual binding of {@code A} using reflection and compares it to the
     * desired argument type. This only works if {@code A} is a concrete type and not a type variable. In that case only
     * the upper bound of {@code A} will be checked, which might be {@link Object}.
     *
     * @param type to check
     * @return whether or not this functor can accept arguments of the given type.
     */
    default boolean acceptsArgumentsOfType(Type type) {
        return resolveRawArguments(Function.class, getClass())[0].isAssignableFrom(toClass(type));
    }

}
