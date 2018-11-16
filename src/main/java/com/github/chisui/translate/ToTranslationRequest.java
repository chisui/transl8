package com.github.chisui.translate;

import com.sun.istack.internal.NotNull;

public final class ToTranslationRequest implements TranslationFunction<TranslationRequest<?, ?>> {

    @Override
    public <K extends TranslationKey<K, A>, A> TranslationRequest<K, A> apply(@NotNull TranslationRequest<K, A> req) {
        return req;
    }

    @Override
    public <T extends Translatable> TranslationRequest<ClassTranslationKey<T>, T> apply(@NotNull T t) {
        return apply(TranslationKey.of(t), t);
    }

    @SuppressWarnings({
            "unchecked", // unchecked cast to actually call non varargs format method.
            "rawtype", // cast through rawtype to let key accept A... instead of A[].
    })
    @Override
    public <K extends TranslationKey<K, A[]>, A> TranslationRequest<K, A[]> apply(@NotNull K key, A... args) {
        return (TranslationRequest<K, A[]>) apply((TranslationKey) key, (Object) args);
    }

    @Override
    public <K extends TranslationKey<K, A>, A> TranslationRequest<K, A> apply(@NotNull K key, A arg) {
        return TranslationRequest.of(key, arg);
    }

}
