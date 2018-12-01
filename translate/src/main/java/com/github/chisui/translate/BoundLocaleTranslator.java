package com.github.chisui.translate;

import java.util.Locale;

import static java.util.Objects.requireNonNull;

public final class BoundLocaleTranslator<R> implements Translator<R> {

    private final Translator<R> parentTranslator;
    private final Locale locale;

    private BoundLocaleTranslator(Translator<R> parentTranslator, Locale locale) {
        this.parentTranslator = requireNonNull(parentTranslator, "parentTranslator");
        this.locale = requireNonNull(locale, "locale");
    }

    public static <R> BoundLocaleTranslator<R> of(Translator<R> parentTranslator, Locale locale) {
        return new BoundLocaleTranslator<>(parentTranslator, locale);
    }

    @Override
    public Locale defaultLocale() {
        return locale;
    }

    @Override
    public <K extends TranslationKey<K, A>, A> R apply(Locale locale, K key, A arg) {
        return parentTranslator.apply(locale, key, arg);
    }

    @Override
    public Translator<R> bindLocale(Locale locale) {
        return new BoundLocaleTranslator<>(parentTranslator, locale);
    }
}
