package com.github.chisui.translate;

import java.util.Objects;

import static java.util.Objects.requireNonNull;


public final class TranslationRequest<K extends TranslationKey<K, ? super A>, A> {

    private final K key;
    private final A arg;

    private TranslationRequest(K key, A arg) {
        this.key = requireNonNull(key, "key");
        this.arg = key.argType() == Void.class
                ? arg : requireNonNull(arg, "arg");
    }

    public static <K extends TranslationKey<K, Void>, Void> TranslationRequest<K, Void> of(K key) {
        return of(key, null);
    }

    public static <K extends TranslationKey<K, ? super A>, A> TranslationRequest<K, A> of(K key, A arg) {
        return new TranslationRequest<>(key, arg);
    }

    public final K key() { return key; }
    public final A arg() { return arg; }

    @Override
    public String toString() {
        return "TranslationRequest{"
                + "key=" + key.toKeyString()
                + (arg == null ? "": ", arg=" + ObjectUtils.toString(arg))
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || TranslationRequest.class != o.getClass()) return false;
        TranslationRequest<?, ?> that = (TranslationRequest<?, ?>) o;
        return Objects.equals(this.key, that.key)
                && ObjectUtils.equals(this.arg, that.arg);

    }

    @Override
    public int hashCode() {
        return Objects.hash(key, arg);
    }
}
