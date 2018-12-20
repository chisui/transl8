package com.github.chisui.translate;

/**
 * Represents a function that accepts three arguments and produces a result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object)}.
 *
 * @param <A> the type of the first input to the function
 * @param <B> the type of the second input to the function
 * @param <C> the type of the third input to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface TriFunction<A, B, C, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param a the first function argument
     * @param b the second function argument
     * @param c the third function argument
     * @return the function result
     */
    R apply(A a, B b, C c);

}
