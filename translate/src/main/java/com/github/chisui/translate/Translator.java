package com.github.chisui.translate;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;
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
 * @param <Return> type of return value
 * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
 */
@FunctionalInterface
public interface Translator<Return> extends TranslationFunction<Return> {

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
     * @param <Key> the key type of the {@link TranslationRequest}
     * @param <Arg> the argument type ot the {@link TranslationRequest}
     * @return the function result
     */
    default <Key extends TranslationKey<Key, Arg>, Arg> Return apply(
            final Locale locale,
            final TranslationRequest<Key, Arg> req) {
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
    default <T extends Translatable> Return apply(
            final Locale locale,
            final T t) {
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
     * @param <Key> self referential key type
     * @return the function result
     */
    default <Key extends TranslationKey<Key, Void>> Return apply(
            final Locale locale,
            final Key key) {
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
     * @param <Key> self referential key type
     * @param <Arg> argument type of the key
     * @return the function result
     */
    @SuppressWarnings({
            "unchecked", // unchecked cast to actually call non varargs format method.
            "rawtype", // cast through rawtype to let key accept Arg... instead of Arg[].
    })
    default <Key extends TranslationKey<Key, Arg[]>, Arg> Return apply(
            final Locale locale,
            final Key key,
            final Arg... args) {
        return (Return) apply(locale, (TranslationKey) key, (Object) args);
    }

    /**
     * Applies the {@link Translator} to the given {@link TranslationKey}, its argument and {@link Locale}.
     *
     * @param locale argument
     * @param key argument
     * @param args arguments the key expects
     * @param <Key> self referential key type
     * @param <Arg> argument type of the key
     * @return the function result
     */
    <Key extends TranslationKey<Key, Arg>, Arg> Return apply(
            final Locale locale,
            final Key key,
            final Arg arg);

    /**
     * Applies the {@link Translator} to the given {@link TranslationKey} and its argument.
     * The default implementation delegates to {@link Translator#apply(Locale, TranslationKey, Object)} by applying the
     * {@link Translator#defaultLocale()}.
     *
     * @param key argument
     * @param args arguments the key expects
     * @param <Key> self referential key type
     * @param <Arg> argument type of the key
     * @return the function result
     */
    default <Key extends TranslationKey<Key, Arg>, Arg> Return apply(
            final Key key,
            final Arg arg) {
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
    default Translator<Return> bindLocale(
            final Locale locale) {
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
    default Translator<Return> bindLocale(
            final Supplier<Locale> locale) {
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
     * @param <Return> return type of the {@link Translator}
     * @return the {@link Translator}
     */
    static <Return> Translator<Return> ofUnsafe(
            final TriFunction<Locale, TranslationKey<?, ?>, Object, Return> f) {
        return f::apply;
    }

    /**
     * Creates a new {@link Translator} from a {@link KeyToString} function, a message source and a function that turns
     * a message into a {@link Formatter}.
     *
     * @param keyToString function
     * @param messageSource function
     * @param toFormatter function
     * @param <Msg> type of message
     * @param <Return> return type of {@link Translator}
     * @return the translator
     */
    static <Msg, Return> FormatterSourceTranslator<Return> of(
            final KeyToString keyToString,
            final BiFunction<
                    ? super String, ? super Locale,
                    Optional<Msg>> messageSource,
            final BiFunction<
                    ? super Msg, ? super Locale,
                    ? extends Formatter<Object, Return>> toFormatter) {
        return new FormatterSourceTranslator<>(FormatterSource.of(keyToString, messageSource, toFormatter));
    }

    /**
     * Creates a new {@link Translator} from the default {@link KeyToString} function, a message source and a function
     * that turns a message into a {@link Formatter}.
     *
     * @param messageSource function
     * @param toFormatter function
     * @param <Msg> type of message
     * @param <Return> return type of {@link Translator}
     * @return the translator
     * @see DefaultKeyToString
     */
    static <Msg, Return> FormatterSourceTranslator<Return> of(
            final BiFunction<
                    ? super String, ? super Locale,
                    Optional<Msg>> messageSource,
            final BiFunction<
                    ? super Msg, ? super Locale,
                    ? extends Formatter<Object, Return>> toFormatter) {
        return of(DefaultKeyToString.instance(), messageSource, toFormatter);
    }
}