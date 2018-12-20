package com.github.chisui.translate;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a function that operates on translatable values of any type in conjunction with a {@link Locale}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Locale, TranslationKey, Object)}.
 *
 * It provides default implementations of {@link TranslationFunction} methods that apply a {@link #defaultLocale()}.
 * <table>
 *     <tr>
 *         <th>{@link TranslationFunction} method</th>
 *         <th>delegates to</th>
 *     </tr>
 *     <tr>
 *         <td>{@link TranslationFunction#apply(TranslationKey, Object)}</td>
 *         <td>{@link Translator#apply(Locale, TranslationKey, Object)}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link TranslationFunction#apply(TranslationKey, Object[])}</td>
 *         <td>{@link Translator#apply(Locale, TranslationKey, Object[])}</td>
 *     </tr>
 *     <tr>
 *        <td>{@link TranslationFunction#apply(TranslationKey)}</td>
 *        <td>{@link Translator#apply(Locale, TranslationKey)}</td>
 *     </tr>
 *     <tr>
 *        <td>{@link TranslationFunction#apply(Translatable)}</td>
 *        <td>{@link Translator#apply(Locale, Translatable)}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link TranslationFunction#apply(TranslationRequest)}</td>
 *         <td>{@link Translator#apply(Locale, TranslationRequest)}</td>
 *     </tr>
 * </table>
 *
 * @param <R> type of return value
 * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
 */
@FunctionalInterface
public interface Translator<R> extends TranslationFunction<R> {

    /**
     * Determines the default Locale to apply if a {@link TranslationFunction} method is called.
     * All method of the {@link TranslationFunction} interface have default implementations on {@link Translator} that
     * delegate to the corresponding {@link Translator} function that expects a {@link Locale} as its first argument
     * this Method is used to retrieve that first argument.
     *
     * The default implementation delegates to {@link Locale#getDefault()}
     *
     * @return the default {@link Locale} to use
     */
    default Locale defaultLocale() {
        return Locale.getDefault();
    }

    /**
     * Applies the {@link Translator} to the given {@link TranslationRequest} and {@link Locale}.
     * The default implementation delegates to {@link Translator#apply(Locale, TranslationKey, Object)} by extracting
     * the key and argument from the {@link TranslationRequest}.
     *
     * @param locale argument
     * @param req the {@link TranslationRequest} argument
     * @param <K> the key type of the {@link TranslationRequest}
     * @param <A> the argument type ot the {@link TranslationRequest}
     * @return the function result
     */
    default <K extends TranslationKey<K, A>, A> R apply(Locale locale, TranslationRequest<K, A> req) {
        return apply(locale, req.key(), req.arg());
    }

    /**
     * Applies the {@link Translator} to the given {@link Translatable} and {@link Locale}.
     * The default implementation delegates to {@link Translator#apply(Locale, TranslationKey, Object)} by creating
     * a {@link TranslationKey} for the translatable and applying the translatable itself as argument.
     *
     * @param locale argument
     * @param t translatable argument
     * @return the function result
     */
    default <T extends Translatable> R apply(Locale locale, T t) {
        return apply(locale, TranslationKey.of(t), t);
    }

    /**
     * Applies the {@link Translator} to the given {@link TranslationKey} that does not expect any arguments and
     * {@link Locale}.
     * The default implementation delegates to {@link Translator#apply(Locale, TranslationKey, Object)} by applying
     * {@code null} as the argument.
     *
     * @param locale argument
     * @param key argument
     * @param <K> self referential key type
     * @return the function result
     */
    default <K extends TranslationKey<K, Void>> R apply(Locale locale, K key) {
        return apply(locale, key, null);
    }

    /**
     * Applies the {@link Translator} to the given {@link TranslationKey} and its arguments and {@link Locale}.
     * The default implementation delegates to {@link Translator#apply(Locale, TranslationKey, Object)} by applying the
     * varargs as and array.
     *
     * @param locale argument
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
    default <K extends TranslationKey<K, A[]>, A> R apply(Locale locale, K key, A... args) {
        return (R) apply(locale, (TranslationKey) key, (Object) args);
    }

    /**
     * Applies the {@link Translator} to the given {@link TranslationKey}, its argument and {@link Locale}.
     *
     * @param locale argument
     * @param key argument
     * @param args arguments the key expects
     * @param <K> self referential key type
     * @param <A> argument type of the key
     * @return the function result
     */
    <K extends TranslationKey<K, A>, A> R apply(Locale locale, K key, A arg);

    /**
     * Applies the {@link Translator} to the given {@link TranslationKey} and its argument.
     * The default implementation delegates to {@link Translator#apply(Locale, TranslationKey, Object)} by applying the
     * {@link Translator#defaultLocale()}.
     *
     * @param key argument
     * @param args arguments the key expects
     * @param <K> self referential key type
     * @param <A> argument type of the key
     * @return the function result
     */
    default <K extends TranslationKey<K, A>, A> R apply(K key, A arg) {
        return apply(defaultLocale(), key, arg);
    }

    /**
     * Create a new {@link Translator} with another default locale. The resulting {@link Translator} will work the same
     * except that {@link #defaultLocale()} will return the provided locale. As a result all inherited methods from
     * {@link TranslationFunction} will use that locale when delegating to the corresponding {@link Translator} function
     *
     * @param locale to bind
     * @return the {@link Translator} with bound default {@link Locale}
     * @see #defaultLocale()
     * @see Translator
     */
    default Translator<R> bindLocale(Locale locale) {
        requireNonNull(locale, "locale");
        return bindLocale(() -> locale);
    }

    /**
     * Create a new {@link Translator} with another default locale. The resulting {@link Translator} will work the same
     * except that {@link #defaultLocale()} will return the provided locale. As a result all inherited methods from
     * {@link TranslationFunction} will use that locale when delegating to the corresponding {@link Translator} function
     *
     * @param locale {@link Supplier} to bind
     * @return the {@link Translator} with bound default {@link Locale}
     * @see #defaultLocale()
     * @see Translator
     */
    default Translator<R> bindLocale(Supplier<Locale> locale) {
        return BoundLocaleTranslator.of(this, locale);
    }

    /**
     * Creates a {@link Translator} from a {@link TriFunction} by using the provided function as the
     * implementation of {@link Translator#apply(Locale, TranslationKey, Object)}.
     * Since {@link Translator#apply(Locale, TranslationKey, Object)} is a generic method it can't be used as the
     * target of a lambda expression. So something like {@code Translator<String> f = (l, k, a) -> ""} wont compile.
     * We work around this issue by turning a non generic {@link BiFunction}, that can be used as the target of a lambda
     * expression into a {@link Translator}. Since the resulting {@link Translator} still has generic
     * functions its contract is not broken. The provided {@link TriFunction} can assume that the provided argument has
     * the expected type.
     *
     * @param f {@link TriFunction} to turn to turn into a {@link Translator}.
     * @param <R> return type of the {@link Translator}
     * @return the {@link Translator}
     */
    static <R> Translator<R> ofUnsafe(TriFunction<Locale, TranslationKey<?, ?>, Object, R> f) {
        return f::apply;
    }

    /**
     * Creates a new {@link Translator} from a {@link KeyToString} function, a message source and a function that turns
     * a message into a {@link Formatter}.
     *
     * @param keyToString function
     * @param messageSource function
     * @param toFormatter function
     * @param <T> type of message
     * @param <R> return type of {@link Translator}
     * @return the translator
     */
    static <T, R> FormatterSourceTranslator<R> of(
            KeyToString keyToString,
            BiFunction<? super String, ? super Locale, Optional<T>> messageSource,
            BiFunction<? super T, ? super Locale, ? extends Formatter<Object, R>> toFormatter) {
        return new FormatterSourceTranslator<>(FormatterSource.of(keyToString, messageSource, toFormatter));
    }

    /**
     * Creates a new {@link Translator} from the default {@link KeyToString} function, a message source and a function
     * that turns a message into a {@link Formatter}.
     *
     * @param messageSource function
     * @param toFormatter function
     * @param <T> type of message
     * @param <R> return type of {@link Translator}
     * @return the translator
     * @see DefaultKeyToString
     */
    static <T, R> FormatterSourceTranslator<R> of(
            BiFunction<? super String, ? super Locale, Optional<T>> messageSource,
            BiFunction<? super T, ? super Locale, ? extends Formatter<Object, R>> toFormatter) {
        return of(DefaultKeyToString.instance(), messageSource, toFormatter);
    }
}