package com.github.chisui.translate;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;


public interface Translator<R> extends TranslationFunction<R> {

    default Locale defaultLocale() {
        return Locale.getDefault();
    }

    default <K extends TranslationKey<K, A>, A> R apply(Locale locale, TranslationRequest<K, A> req) {
        return apply(locale, req.key(), req.arg());
    }

    default <T extends Translatable> R apply(Locale locale, T t) {
        return apply(locale, TranslationKey.of(t), t);
    }

    default <K extends TranslationKey<K, Void>> R apply(Locale locale, K key) {
        return apply(locale, key, null);
    }

    @SuppressWarnings({
            "unchecked", // unchecked cast to actually call non varargs format method.
            "rawtype", // cast through rawtype to let key accept A... instead of A[].
    })
    default <K extends TranslationKey<K, A[]>, A> R apply(Locale locale, K key, A... args) {
        return (R) apply(locale, (TranslationKey) key, (Object) args);
    }

    <K extends TranslationKey<K, A>, A> R apply(Locale locale, K key, A arg);

    default <K extends TranslationKey<K, A>, A> R apply(K key, A arg) {
        return apply(defaultLocale(), key, arg);
    }

    default Translator<R> bindLocale(Locale locale) {
        return BoundLocaleTranslator.of(this, locale);
    }

    static <R> Translator<R> ofUnsafe(TriFunction<Locale, TranslationKey<?, ?>, Object, R> f) {
        return f::apply;
    }

    static <T, R> ComposedTranslator<T, R> of(
            KeyToString keyToString,
            BiFunction<? super Locale, ? super String, T> messageSource,
            Function<? super T, ? extends Formatter<Object, R>> toFormatter) {
        return new ComposedTranslator<>(keyToString, messageSource, toFormatter);
    }

    static <T, R> ComposedTranslator<T, R> of(
            BiFunction<? super Locale, ? super String, T> messageSource,
            Function<? super T, ? extends Formatter<Object, R>> toFormatter) {
        return of(DefaultKeyToString.instance(), messageSource, toFormatter);
    }


}
