package com.github.chisui.translate;

import com.sun.istack.internal.NotNull;

public interface TranslationFunction<R> {

    default <K extends TranslationKey<K, A>, A> R apply(@NotNull TranslationRequest<K, A> req) {
        return apply(req.key(), req.arg());
    }

    default <T extends Translatable> R apply(@NotNull T t) {
        return apply(TranslationKey.of(t), t);
    }

    @SuppressWarnings({
            "unchecked", // unchecked cast to actually call non varargs format method.
            "rawtype", // cast through rawtype to let key accept A... instead of A[].
    })
    default <K extends TranslationKey<K, A[]>, A> R apply(@NotNull K key, A... args) {
        return (R) apply((TranslationKey) key, (Object) args);
    }

    <K extends TranslationKey<K, A>, A> R apply(@NotNull K key, A arg);

}
