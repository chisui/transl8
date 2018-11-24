package com.github.chisui.translate;

import static java.util.Objects.requireNonNull;

@Value
public final class TranslationRequest<K extends TranslationKey<K, ? super A>, A> {

    private final K key;
    private final A arg;

    private TranslationRequest(K key, A arg) {
        this.key = requireNonNull(key, "key");
        this.arg = requireNonNull(arg, "arg");
    }

    public static <K extends TranslationKey<K, ? super A>, A> TranslationRequest<K, A> of(K key, A arg) {
        return new TranslationRequest<>(key, arg);
    }

    public final K key() { return key; }
    public final A arg() { return arg; }

}
