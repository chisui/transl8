package com.github.chisui.translate;

import java.util.Locale;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Arg {@link Translator} that binds a {@link Locale} to an existing {@link Translator}. The bound {@link Locale} will be
 * used when calling the {@link TranslationFunction} functionality using by providing it through the
 * {@link #defaultLocale()} method. All other functionality will delegate to the underlying {@link Translator}.
 *
 * @param <Return> the return type of the delegated {@link Translator}
 */
public final class BoundLocaleTranslator<Return>
        implements Translator<Return> {

    private final Translator<Return> parentTranslator;
    private final Supplier<Locale> locale;

    private BoundLocaleTranslator(
            final Translator<Return> parentTranslator,
            final Supplier<Locale> locale) {
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
    public static <R> BoundLocaleTranslator<R> of(
            final Translator<R> parentTranslator,
            final Supplier<Locale> locale) {
        return new BoundLocaleTranslator<>(parentTranslator, locale);
    }

    @Override
    public Locale defaultLocale() {
        return locale().get();
    }

    @Override
    public <Key extends TranslationKey<Key, Arg>, Arg> Return apply(
            final Locale locale,
            final Key key,
            final Arg arg) {
        return parentTranslator().apply(locale, key, arg);
    }

    @Override
    public Translator<Return> bindLocale(Supplier<Locale> locale) {
        return new BoundLocaleTranslator<>(parentTranslator, locale);
    }

    public Translator<Return> parentTranslator() {
        return parentTranslator;
    }

    public Supplier<Locale> locale() {
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
    public boolean equals(
            final Object o) {
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
