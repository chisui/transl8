package com.github.chisui.translate;

public interface Translator<R> {

    default <T extends Translatable> R format(T t) {
        return format(TranslationKey.of(t), t);
    }

    @SuppressWarnings({
            "unchecked", // unchecked cast to actually call non varargs format method.
            "rawtype", // cast through rawtype to let key accept A... instead of A[].
    })
    default <K extends TranslationKey<K, A[]>, A> R format(K key, A... args) {
        return (R) format((TranslationKey) key, (Object) args);
    }

    <K extends TranslationKey<K, A>, A> R format(K key, A arg);

}
