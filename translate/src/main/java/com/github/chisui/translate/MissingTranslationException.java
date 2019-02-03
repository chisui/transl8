package com.github.chisui.translate;

import java.util.Locale;

import static java.util.Objects.requireNonNull;

public class MissingTranslationException extends RuntimeException {

    private final String source;
    private final Locale locale;
    private final String key;

    public MissingTranslationException(
            final String source,
            final Locale locale,
            final TranslationKey<?, ?> key) {
        this.source = requireNonNull(source, "source");
        this.locale = requireNonNull(locale, "locale");
        this.key = requireNonNull(key, "key").toKeyString();
    }

    @Override
    public String getMessage() {
        return String.format(
                "Could not find translation for %s:%s in \"%s\"",
                locale(), key(), source());
    }

    public String source() {
        return source;
    }

    public Locale locale() {
        return locale;
    }

    public String key() {
        return key;
    }
}
