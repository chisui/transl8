package com.github.chisui.translate;

import java.util.Locale;

import static java.util.Objects.requireNonNull;

public class FormatterSourceTranslator<R> implements Translator<R> {

    private final FormatterSource<R> formatterSource;

    public FormatterSourceTranslator(FormatterSource<R> formatterSource) {
        this.formatterSource = requireNonNull(formatterSource, "formatterSource");
    }

    @Override
    public <K extends TranslationKey<K, A>, A> R apply(Locale locale, K key, A arg) {
        return formatterSource.formatterOf(locale, key)
                .orElseThrow(() -> new MissingTranslationException(formatterSource.toString(), locale, key))
                .apply(this, arg);
    }
}
