package com.github.chisui.translate;

import java.lang.reflect.Type;

/**
 * Represents a dynamic function that can verify its argument type.
 * What interface is actually used to represent the function call itself it not determined by this interface.
 * Instead this interface is meant as an addition to other functional interfaces:
 *
 * <pre>
 *     interface MyFun<A, B> extends Function<A, B>, VerifyArguments {}
 * </pre>
 *
 * Or even:
 *
 * <pre>
 *     <F extends Predicate<A> & VerifyArguments, A>
 * </pre>
 *
 * This interface is only meant to be used in conjunction with unary functional interfaces like
 * {@link java.util.function.Function} or {@link java.util.function.Predicate}.
 */
@FunctionalInterface
public interface VerifyArguments {

    /**
     * Checks whether or not this functor object can accept arguments of the provided type.
     *
     * @param type to check
     * @return whether or not this functor can accept arguments of the given type.
     */
    boolean acceptsArgumentsOfType(Type type);
}
