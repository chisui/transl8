package net.chisui.transl8;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Locale;
import java.util.function.Supplier;

import net.chisui.transl8.format.Format;
import net.chisui.transl8.format.Formatable;
import net.chisui.transl8.lookup.MessageLookup;
import net.chisui.transl8.lookup.MessageSource;

/**
 * Interface for Translators that turn a {@link TranslationHint} into a {@link String} representation.
 */
public interface Translator {

	/**
	 * Translates an arbitrary {@link Object} to {@link String}.
	 * 
	 * {@link TranslationHint TranslationHints} are translated using {@link #translate(TranslationHint)}.
	 * {@link Translatable Translatables} are translated using {@link #translate(Translatable)}.
	 * Otherwise a {@link TranslationHint} with the argument as key and its {@link String#valueOf(Object)} 
	 * as fallback and translated using {@link #translate(TranslationHint)}.
	 * 
	 * @param obj to translate
	 * @return the translation
	 */
	default String translate(final Object obj) {
		if (obj instanceof TranslationHint) {
			return translate((TranslationHint) obj);
		} else if (obj instanceof Translatable) {
			return translate((Translatable) obj);
		} else {
			return translate(TranslationHint
					.of(obj)
					.withFallback(String.valueOf(obj)));
		}
	}

	/**
	 * Translates a {@link Translatable} to {@link String}.
	 * 
	 * This passes the {@link TranslationHint} retrieved with {@link Translatable#getTranslationHint()} to 
	 * {@link #translate(TranslationHint)}.
	 * 
	 * @param translatable to translate
	 * @return the translation
	 */
	default String translate(final Translatable translatable) {
		return translate(translatable.getTranslationHint());
	}

	/**
	 * Translates a {@link TranslationHint} to {@link String}.
	 * 
	 * The {@link TranslationHint} may not be <code>null</code> otherwise a {@link NullPointerException} may
	 * be thrown. The returned String is never <code>null</code>.
	 * 
	 * @param hint to translate
	 * @return the translation
	 */
	String translate(TranslationHint hint);

	/**
	 * Translates an arbitrary {@link Object} to {@link String} relative to the provided {@link Locale}.
	 * 
	 * {@link TranslationHint TranslationHints} are translated using {@link #translate(Locale, TranslationHint)}.
	 * {@link Translatable Translatables} are translated using {@link #translate(Locale, Translatable)}.
	 * Otherwise a {@link TranslationHint} with the argument as key and its {@link String#valueOf(Object)} 
	 * as fallback and translated using {@link #translate(Locale, TranslationHint)}.
	 * 
	 * @param locale of the translation
	 * @param obj to translate
	 * @return the translation
	 */
	default String translate(final Locale locale, final Object obj) {
		if (obj instanceof TranslationHint) {
			return translate(locale, (TranslationHint) obj);
		} else if (obj instanceof Translatable) {
			return translate(locale, (Translatable) obj);
		} else {
			return translate(locale, TranslationHint
					.of(obj)
					.withFallback(String.valueOf(obj)));
		}
	}

	/**
	 * Translates a {@link Translatable} to {@link String} relative to the provided {@link Locale}.
	 * 
	 * This passes the {@link TranslationHint} retrieved with {@link Translatable#getTranslationHint()} to 
	 * {@link #translate(Locale, TranslationHint)}.
	 * 
	 * @param locale of the translation
	 * @param translatable to translate
	 * @return the translation
	 */
	default String translate(final Locale locale, final Translatable translatable) {
		return translate(locale, translatable.getTranslationHint());
	}

	/**
	 * Translates a {@link TranslationHint} to {@link String} relative to the provided {@link Locale}.
	 * 
	 * The {@link TranslationHint} may not be <code>null</code> otherwise a {@link NullPointerException} may
	 * be thrown. The returned String is never <code>null</code>.
	 * 
	 * @param locale of the translation
	 * @param hint to translate
	 * @return the translation
	 */
	String translate(Locale locale, TranslationHint hint);


	/**
	 * Translates an arbitrary {@link Object} and appends the translation to provided {@link Appendable}.
	 * 
	 * {@link TranslationHint TranslationHints} are translated using {@link #translate(Appendable, TranslationHint)}.
	 * {@link Translatable Translatables} are translated using {@link #translate(Appendable, Translatable)}.
	 * Otherwise a {@link TranslationHint} with the argument as key and its {@link String#valueOf(Object)} 
	 * as fallback and translated using {@link #translate(Appendable, TranslationHint)}.
	 * 
	 * @param appendable to append translation to
	 * @param obj to translate
	 * @return the {@link Appendable}
	 */
	default <A extends Appendable> A translate(final A appendable, final Object obj) throws IOException {
		if (obj instanceof TranslationHint) {
			return translate(appendable, (TranslationHint) obj);
		} else if (obj instanceof Translatable) {
			return translate(appendable, (Translatable) obj);
		} else {
			return translate(appendable, TranslationHint
					.of(obj)
					.withFallback(String.valueOf(obj)));
		}
	}

	default <A extends Appendable> A translate(final A appendable, final Translatable translatable) throws IOException {
		return translate(appendable, translatable.getTranslationHint());
	}

	<A extends Appendable> A translate(A appendable, TranslationHint hint) throws IOException;

	default <A extends Appendable> A translate(final A appendable, final Locale locale, final Object obj) throws IOException {
		if (obj instanceof TranslationHint) {
			return translate(appendable, locale, (TranslationHint) obj);
		} else if (obj instanceof Translatable) {
			return translate(appendable, locale, (Translatable) obj);
		} else {
			return translate(appendable, locale, TranslationHint
					.of(obj)
					.withFallback(String.valueOf(obj)));
		}
	}

	default <A extends Appendable> A translate(final A appendable, final Locale locale, final Translatable translatable)
			throws IOException {
		return translate(appendable, locale, translatable.getTranslationHint());
	}

	<A extends Appendable> A translate(A appendable, Locale locale, TranslationHint hint) throws IOException;
	
    default StringBuilder translate(final StringBuilder appendable, final Object obj) {
        if (obj instanceof TranslationHint) {
            return translate(appendable, (TranslationHint) obj);
        } else if (obj instanceof Translatable) {
            return translate(appendable, (Translatable) obj);
        } else {
            return translate(appendable, TranslationHint
                    .of(obj)
                    .withFallback(String.valueOf(obj)));
        }
    }
    
    default StringBuilder translate(final StringBuilder appendable, final Translatable translatable) {
        return translate(appendable, translatable.getTranslationHint());
    }
    
    default StringBuilder translate(final StringBuilder appendable, final TranslationHint hint) {
    	try {
			return (StringBuilder) translate((Appendable) appendable, hint);
		} catch (final IOException e) {
			throw new AssertionError("translating using StringBuilder threw an IOException", e);
		}
    }
    
    default StringBuilder translate(final StringBuilder appendable, final Locale locale, final Object obj) {
        if (obj instanceof TranslationHint) {
            return translate(appendable, locale, (TranslationHint) obj);
        } else if (obj instanceof Translatable) {
            return translate(appendable, locale, (Translatable) obj);
        } else {
            return translate(appendable, locale, TranslationHint
                    .of(obj)
                    .withFallback(String.valueOf(obj)));
        }
    }
    
    default StringBuilder translate(final StringBuilder appendable, final Locale locale, final Translatable translatable) {
        return translate(appendable, locale, translatable.getTranslationHint());
    }
    
    default StringBuilder translate(final StringBuilder appendable, final Locale locale, final TranslationHint hint) {
    	try {
			return (StringBuilder) translate((Appendable) appendable, locale, hint);
		} catch (final IOException e) {
			throw new AssertionError("translating using StringBuilder threw an IOException", e);
		}
    }

	abstract class AbstractTranslator implements Translator {

		protected final Supplier<Locale> getDefaultLocale;
		
		public AbstractTranslator(final Supplier<Locale> getDefaultLocale) {
			this.getDefaultLocale = requireNonNull(getDefaultLocale);
		}

		@Override
		public String translate(final TranslationHint hint) {
			return translate(getDefaultLocale.get(), hint);
		}

		@Override
		public <A extends Appendable> A translate(
				final A appendable, 
				final TranslationHint hint) throws IOException {
			return translate(appendable, getDefaultLocale.get(), hint);
		}

	}
	
	static StringFirstTranslator of(
			final Supplier<Locale> getDefaultLocale, 
			final TranslationFunction translate) {
		return new StringFirstTranslator(getDefaultLocale, translate);
	}
	
	static StringFirstTranslator of(
			final TranslationFunction translate) {
		return of(Locale::getDefault, translate);
	}
	
	@FunctionalInterface
	interface TranslationFunction {
		
		String translate(Locale locale, TranslationHint hint, Translator translator);
		
	}
	
	class StringFirstTranslator extends AbstractTranslator {

		protected final TranslationFunction translate;

		public StringFirstTranslator(
				final Supplier<Locale> getDefaultLocale, 
				final TranslationFunction translate) {
			super(getDefaultLocale);
			this.translate = requireNonNull(translate);
		}

		@Override
		public String translate(final Locale locale, final TranslationHint hint) {
			return translate.translate(locale, hint, this);
		}

		@Override
		public <A extends Appendable> A translate(final A appendable, final Locale locale, final TranslationHint hint) throws IOException {
			appendable.append(translate(locale, hint));
			return appendable;
		}
	}
	
	static AppendableFirstTranslator of(
			final Supplier<Locale> getDefaultLocale, 
			final AppendingTranslationFunction translate) {
		return new AppendableFirstTranslator(getDefaultLocale, translate);
	}
	
	static AppendableFirstTranslator of(
			final AppendingTranslationFunction translate) {
		return of(Locale::getDefault, translate);
	}
	
	@FunctionalInterface
	interface AppendingTranslationFunction {
		
		void translate(Appendable appendable, Locale locale, TranslationHint hint, Translator translator) throws IOException;
		
	}
	
	class AppendableFirstTranslator extends AbstractTranslator {
		
		protected final AppendingTranslationFunction translate;
		
		public AppendableFirstTranslator(
				final Supplier<Locale> getDefaultLocale, 
				final AppendingTranslationFunction translate) {
			super(getDefaultLocale);
			this.translate = requireNonNull(translate);
		}

		@Override
		public String translate(final Locale locale, final TranslationHint hint) {
			return translate(new StringBuilder(), locale, hint).toString();
		}

		@Override
		public <A extends Appendable> A translate(final A appendable, final Locale locale, final TranslationHint hint) throws IOException {
			translate.translate(appendable, locale, hint, this);
			return appendable;
		}
		
	}

	static Translator of(
			final MessageSource messageSource,
			final Format format) {
		return of(MessageLookup.of(messageSource), format);
	}

	static Translator of(
			final MessageLookup messageLookup,
			final Format format) {
		return of(Locale::getDefault, messageLookup, format);
	}
	
	static Translator of(
			final Supplier<Locale> getDefaultLocale,
			final MessageSource messageSource,
			final Format format) {
		return of(getDefaultLocale, MessageLookup.of(messageSource), format);
	}
	
	static Translator of(
			final Supplier<Locale> getDefaultLocale,
			final MessageLookup messageLookup,
			final Format format) {
		requireNonNull(getDefaultLocale);
		requireNonNull(messageLookup);
		requireNonNull(format);
		return of(getDefaultLocale, (appendable, locale, hint, translator) -> {
			final String message = messageLookup.getMessage(locale, hint.getKey(), hint.getFallback());
			final Formatable formatable = format.toFormatable(locale, message);
			formatable.format(appendable, hint.getArguments(), translator);
		});
	}
	
}
