package com.github.chisui.translate;

import java.util.Locale;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Translator} that binds a {@link Locale} to an existing {@link Translator}. The bound {@link Locale} will be
 * used when calling the {@link TranslationFunction} functionality using by providing it through the
 * {@link #defaultLocale()} method. All other functionality will delegate to the underlying {@link Translator}.
 *
 * @param <R> the return type of the delegated {@link Translator}
 */
public final class BoundLocaleTranslator<R> implements Translator<R> {

    private final Translator<R> parentTranslator;
    private final Supplier<Locale> locale;

    private BoundLocaleTranslator(Translator<R> parentTranslator, Supplier<Locale> locale) {
        this.parentTranslator = requireNonNull(parentTranslator, "parentTranslator");
        this.locale = requireNonNull(locale, "locale");
    }

    /**
     * Creates a new {@link BoundLocaleTranslator} from a {@link Translator} to delegate to and a {@link Supplier} for
     * the bound {@link Locale}. None of the provided arguments may be {@code null} nor may the {@link Supplier} return
     * {@code null}.
     *
     * @param parentTranslator to delegate to
     * @param locale to use as bound default
     * @param <R> return type of the delegated {@link Translator}
     * @return the {@link BoundLocaleTranslator}
     */
    public static <R> BoundLocaleTranslator<R> of(Translator<R> parentTranslator, Supplier<Locale> locale) {
        return new BoundLocaleTranslator<>(parentTranslator, locale);
    }

    @Override
    public Locale defaultLocale() {
        return getLocale().get();
    }

    @Override
    public <K extends TranslationKey<K, A>, A> R apply(Locale locale, K key, A arg) {
        return getParentTranslator().apply(locale, key, arg);
    }

    @Override
    public Translator<R> bindLocale(Supplier<Locale> locale) {
        return new BoundLocaleTranslator<>(parentTranslator, locale);
    }

    public Translator<R> getParentTranslator() {
        return parentTranslator;
    }

    public Supplier<Locale> getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return "BoundLocaleTranslator{" +
                "parentTranslator=" + parentTranslator +
                ", locale=" + locale +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundLocaleTranslator<?> that = (BoundLocaleTranslator<?>) o;
        return parentTranslator.equals(that.parentTranslator) &&
                locale.equals(that.locale);
    }

    @Override
    public int hashCode() {
        return parentTranslator.hashCode() * 31 + locale.hashCode();
    }
}
