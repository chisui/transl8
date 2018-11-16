package com.github.chisui.translate;

public interface TranslationFunction<R> {

    default <K extends TranslationKey<K, A>, A> R apply(TranslationRequest<K, A> req) {
        return apply(req.key(), req.arg());
    }

    default <T extends Translatable> R apply(T t) {
        return apply(TranslationKey.of(t), t);
    }

    @SuppressWarnings({
            "unchecked", // unchecked cast to actually call non varargs format method.
            "rawtype", // cast through rawtype to let key accept A... instead of A[].
    })
    default <K extends TranslationKey<K, A[]>, A> R apply(K key, A... args) {
        return (R) apply((TranslationKey) key, (Object) args);
    }

    <K extends TranslationKey<K, A>, A> R apply(K key, A arg);

}
