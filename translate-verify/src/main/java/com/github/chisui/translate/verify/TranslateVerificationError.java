package com.github.chisui.translate.verify;

import com.github.chisui.translate.DefaultKeyToString;
import com.github.chisui.translate.Formatter;
import com.github.chisui.translate.TranslationKey;

import java.util.Locale;

import static com.github.chisui.translate.DefaultKeyToString.defaultToKeyString;
import static java.util.Objects.requireNonNull;

public abstract class TranslateVerificationError {
    private final TranslationKey<?, ?> key;
    private final Locale locale;

    private TranslateVerificationError(TranslationKey<?, ?> key, Locale locale) {
        this.key = requireNonNull(key);
        this.locale =requireNonNull(locale);
    }

    public TranslationKey<?, ?> getKey() {
        return key;
    }

    public Locale getLocale() {
        return locale;
    }

    public abstract String toString();

    public static class TypeMismatch extends TranslateVerificationError {

        private final Formatter<?, ?> formatter;

        public TypeMismatch(TranslationKey<?, ?> key, Locale locale, Formatter<?, ?> formatter) {
            super(key, locale);
            this.formatter = requireNonNull(formatter);
        }

        public Formatter<?, ?> getFormatter() {
            return formatter;
        }

        @Override
        public String toString() {
            return String.format("Type mismatch: %s:%s expects type %s but formatter %s does not accept that type",
                    defaultToKeyString(getKey()), getLocale(), getKey().argType(), getFormatter());
        }
    }

    public static class MissingFormatter extends TranslateVerificationError {

        public MissingFormatter(TranslationKey<?, ?> key, Locale locale) {
            super(key, locale);
        }

        @Override
        public String toString() {
            return String.format("Translation missing for %s:%s", defaultToKeyString(getKey()), getLocale());
        }
    }
}
