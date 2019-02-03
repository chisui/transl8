package com.github.chisui.translate;

import java.util.Locale;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Translator} that is backed by a {@link FormatterSource}.
 *
 * @param <Return> return type of the {@link Formatter}
 */
public class FormatterSourceTranslator<Return> implements Translator<Return> {

    private final FormatterSource<Return> formatterSource;

    FormatterSourceTranslator(FormatterSource<Return> formatterSource) {
        this.formatterSource = requireNonNull(formatterSource, "formatterSource");
    }

    @Override
    public <K extends TranslationKey<K, A>, A> Return apply(Locale locale, K key, A arg) {
        return formatterSource.formatterOf(locale, key)
                .orElseThrow(() -> new MissingTranslationException(formatterSource.toString(), locale, key))
                .apply(this, arg);
    }
}
