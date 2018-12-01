package com.github.chisui.translate;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public final class ResourceBundleMessageSource implements BiFunction<Locale, String, Optional<String>> {

    private final String bundleName;

    private ResourceBundleMessageSource(String bundleName) {
        this.bundleName = bundleName;
    }

    public static Optional<ResourceBundleMessageSource> of(String bundleName) {
        requireNonNull(bundleName, "bundleName");
        try {
            ResourceBundle.getBundle(bundleName);
            return Optional.of(new ResourceBundleMessageSource(bundleName));
        } catch (MissingResourceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> apply(Locale locale, String key) {
        return Optional.of(ResourceBundle.getBundle(bundleName, locale))
                .filter(bundle -> bundle.containsKey(key)) // prevent ResourceNotFoundException
                .map(bundle -> bundle.getObject(key))
                .filter(String.class::isInstance)
                .map(String.class::cast);
    }
}
