package com.github.chisui.translate;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;
import static java.util.ResourceBundle.getBundle;

/**
 * A lookup for messages that is backed by a {@link ResourceBundle}.
 */
public final class ResourceBundleMessageSource
        implements BiFunction<String, Locale, Optional<String>> {

    private final String bundleName;

    private ResourceBundleMessageSource(
            final String bundleName) {
        this.bundleName = requireNonNull(bundleName, "bundleName");
    }

    public static ResourceBundleMessageSource of(String bundleName) {
        return new ResourceBundleMessageSource(bundleName);
    }

    @Override
    public Optional<String> apply(String key, Locale locale) {
        try {
            return Optional.of(getBundle(bundleName, locale))
                    .map(bundle -> bundle.getObject(key))
                    .filter(String.class::isInstance)
                    .map(String.class::cast);
        } catch (MissingResourceException e) {
            return Optional.empty();
        }
    }
}
