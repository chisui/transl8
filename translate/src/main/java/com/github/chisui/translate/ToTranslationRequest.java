package com.github.chisui.translate;


/**
 * A {@link TranslationFunction} that wraps all calls in a {@link TranslationRequest}.
 */
public enum ToTranslationRequest implements TranslationFunction<TranslationRequest<?, ?>> {
    INSTANCE;

    @Override
    public <K extends TranslationKey<K, A>, A> TranslationRequest<K, A> apply(TranslationRequest<K, A> req) {
        return req;
    }

    @Override
    public <T extends Translatable> TranslationRequest<ClassTranslationKey<T>, T> apply(T t) {
        return apply(TranslationKey.of(t), t);
    }

    @Override
    public <K extends TranslationKey<K, Void>> TranslationRequest<K, Void> apply(K key) {
        return apply(key, null);
    }

    @SuppressWarnings({
            "unchecked", // unchecked cast to actually call non varargs format method.
            "rawtype", // cast through rawtype to let key accept A... instead of A[].
    })
    @Override
    public <K extends TranslationKey<K, A[]>, A> TranslationRequest<K, A[]> apply(K key, A... args) {
        return (TranslationRequest<K, A[]>) apply((TranslationKey) key, (Object) args);
    }

    @Override
    public <K extends TranslationKey<K, A>, A> TranslationRequest<K, A> apply(K key, A arg) {
        return TranslationRequest.of(key, arg);
    }

}
