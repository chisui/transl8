package com.github.chisui.translate.example;

import com.github.chisui.translate.Translatable;
import com.github.chisui.translate.TranslationHint;

import static java.util.Objects.requireNonNull;
import static com.github.chisui.translate.Translator.fallbackTranslator;


public class User implements Translatable {
    private final String first;
    private final String last;

    public User(String first, String last) {
        this.first = requireNonNull(first, "first");
        this.last = requireNonNull(last, "last");
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    @Override
    public TranslationHint getTranslationHint() {
        return TranslationHint.of(getClass())
                .withArguments(getFirst(), getLast())
                .withFallback("{0} {1}");
    }

    @Override
    public java.lang.String toString() {
        return fallbackTranslator().translate(this);
    }
}
