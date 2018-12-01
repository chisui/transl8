package com.github.chisui.translate;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

public final class ResourceBundleMessageSource implements BiFunction<Locale, String, Optional<String>> {

    private final String bundlename;

    public ResourceBundleMessageSource(String bundlename) {
        this.bundlename = Objects.requireNonNull(bundlename, "bundlename");
    }

    @Override
    public Optional<String> apply(Locale locale, String key) {
        return Optional.of(ResourceBundle.getBundle(bundlename, locale))
                .filter(bundle -> bundle.containsKey(key)) // prevent ResourceNotFoundException
                .map(bundle -> bundle.getObject(key))
                .filter(String.class::isInstance)
                .map(String.class::cast);
    }
}
