package com.github.chisui.translate;

import java.util.function.BiFunction;

/**
 * Represents a function that operates on translatable values of any type.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(TranslationKey, Object)}.
 *
 * @param <R> type of return value
 * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
 */
@FunctionalInterface
public interface TranslationFunction<R> {

    /**
     * Applies the {@link TranslationFunction} to the given {@link TranslationRequest}.
     * The default implementation delegates to {@link TranslationFunction#apply(TranslationKey, Object)} by extracting
     * the key and argument from the {@link TranslationRequest}.
     *
     * @param req the {@link TranslationRequest} argument
     * @param <K> the key type of the {@link TranslationRequest}
     * @param <A> the argument type ot the {@link TranslationRequest}
     * @return the function result
     */
    default <K extends TranslationKey<K, A>, A> R apply(TranslationRequest<K, A> req) {
        return apply(req.key(), req.arg());
    }

    /**
     * Applies the {@link TranslationFunction} to the given {@link Translatable}.
     * The default implementation delegates to {@link TranslationFunction#apply(TranslationKey, Object)} by creating
     * a {@link TranslationKey} for the translatable and applying the translatable itself as argument.
     *
     * @param t translatable argument
     * @return the function result
     */
    default <T extends Translatable> R apply(T t) {
        return apply(TranslationKey.of(t), t);
    }

    /**
     * Applies the {@link TranslationFunction} to the given {@link TranslationKey} that does not expect any arguments.
     * The default implementation delegates to {@link TranslationFunction#apply(TranslationKey, Object)} by applying
     * {@code null} as the argument.
     *
     * @param key argument
     * @param <K> self referential key type
     * @return the function result
     */
    default <K extends TranslationKey<K, Void>> R apply(K key) {
        return apply(key, null);
    }

    /**
     * Applies the {@link TranslationFunction} to the given {@link TranslationKey} and its arguments.
     * The default implementation delegates to {@link TranslationFunction#apply(TranslationKey, Object)} by applying the
     * varargs as and array.
     *
     * @param key argument
     * @param args arguments the key expects
     * @param <K> self referential key type
     * @param <A> argument type of the key
     * @return the function result
     */
    @SuppressWarnings({
            "unchecked", // unchecked cast to actually call non varargs format method.
            "rawtype", // cast through rawtype to let key accept A... instead of A[].
    })
    default <K extends TranslationKey<K, A[]>, A> R apply(K key, A... args) {
        return (R) apply((TranslationKey) key, (Object) args);
    }

    /**
     * Applies the {@link TranslationFunction} to the given {@link TranslationKey} and its argument.
     *
     * @param key argument
     * @param args arguments the key expects
     * @param <K> self referential key type
     * @param <A> argument type of the key
     * @return the function result
     */
    <K extends TranslationKey<K, A>, A> R apply(K key, A arg);

    /**
     * Creates a {@link TranslationFunction} from a {@link BiFunction} by using the provided function as the
     * implementation of {@link TranslationFunction#apply(TranslationKey, Object)}.
     * Since {@link TranslationFunction#apply(TranslationKey, Object)} is a generic method it can't be used as the
     * target of a lambda expression. So something like {@code TranslationFunction<String> f = (k, a) -> ""} wont compile.
     * We work around this issue by turning a non generic {@link BiFunction}, that can be used as the target of a lambda
     * expression into a {@link TranslationFunction}. Since the resulting {@link TranslationFunction} still has generic
     * functions its contract is not broken. The provided {@link BiFunction} can assume that the provided argument has
     * the expected type.
     *
     * @param f {@link BiFunction} to turn to turn into a {@link TranslationFunction}.
     * @param <R> return type of the {@link TranslationFunction}
     * @return the {@link TranslationFunction}
     */
    static <R> TranslationFunction<R> ofUnsafe(BiFunction<? super TranslationKey<?, ?>, Object, R> f) {
        return f::apply;
    }
}
