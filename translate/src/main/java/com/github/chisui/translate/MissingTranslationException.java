package com.github.chisui.translate;

import java.util.Locale;

import static java.util.Objects.requireNonNull;

public class MissingTranslationException extends RuntimeException {

    private final String source;
    private final Locale locale;
    private final TranslationKey<?, ?> key;

    public MissingTranslationException(String source, Locale locale, TranslationKey<?, ?> key) {
        this.source = requireNonNull(source, "source");
        this.locale = requireNonNull(locale, "locale");
        this.key = requireNonNull(key, "key");
    }

    @Override
    public String getMessage() {
        return "Could not find translation for " + locale + ':' + key.toKeyString() + " in " + source;
    }
}
