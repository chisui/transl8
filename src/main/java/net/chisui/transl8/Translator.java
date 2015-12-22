package net.chisui.transl8;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.chisui.transl8.format.Format;
import net.chisui.transl8.format.Formatable;
import net.chisui.transl8.lookup.MessageLookup;

/**
 * Interface for all Translators.
 */
public interface Translator {

	default String translate(Object obj) {
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

	default String translate(Translatable translatable) {
		return translate(translatable.getTranslationHint());
	}

	String translate(TranslationHint hint);

	default String translate(Locale locale, Object obj) {
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

	default String translate(Locale locale, Translatable translatable) {
		return translate(locale, translatable.getTranslationHint());
	}

	String translate(Locale locale, TranslationHint hint);

	default <A extends Appendable> A translate(A appendable, Object obj) throws IOException {
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

	default <A extends Appendable> A translate(A appendable, Translatable translatable) throws IOException {
		return translate(appendable, translatable.getTranslationHint());
	}

	<A extends Appendable> A translate(A appendable, TranslationHint hint) throws IOException;

	default <A extends Appendable> A translate(A appendable, Locale locale, Object obj) throws IOException {
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

	default <A extends Appendable> A translate(A appendable, Locale locale, Translatable translatable)
			throws IOException {
		return translate(appendable, locale, translatable.getTranslationHint());
	}

	<A extends Appendable> A translate(A appendable, Locale locale, TranslationHint hint) throws IOException;
	
    default StringBuilder translate(StringBuilder appendable, Object obj) {
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
    
    default StringBuilder translate(StringBuilder appendable, Translatable translatable) {
        return translate(appendable, translatable.getTranslationHint());
    }
    
    default StringBuilder translate(StringBuilder appendable, TranslationHint hint) {
    	try {
			return (StringBuilder) translate((Appendable) appendable, hint);
		} catch (IOException e) {
			throw new AssertionError("translating using StringBuilder threw an IOException", e);
		}
    }
    
    default StringBuilder translate(StringBuilder appendable, Locale locale, Object obj) {
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
    
    default StringBuilder translate(StringBuilder appendable, Locale locale, Translatable translatable) {
        return translate(appendable, locale, translatable.getTranslationHint());
    }
    
    default StringBuilder translate(StringBuilder appendable, Locale locale, TranslationHint hint) {
    	try {
			return (StringBuilder) translate((Appendable) appendable, locale, hint);
		} catch (IOException e) {
			throw new AssertionError("translating using StringBuilder threw an IOException", e);
		}
    }

	abstract class AbstractTranslator implements Translator {

		protected final Supplier<Locale> getDefaultLocale;
		
		public AbstractTranslator(final Supplier<Locale> getDefaultLocale) {
			this.getDefaultLocale = requireNonNull(getDefaultLocale);
		}

		@Override
		public String translate(TranslationHint hint) {
			return translate(getDefaultLocale.get(), hint);
		}

		@Override
		public <A extends Appendable> A translate(A appendable, TranslationHint hint) throws IOException {
			return translate(appendable, getDefaultLocale.get(), hint);
		}

	}
	
	static StringFirstTranslator of(
			Supplier<Locale> getDefaultLocale, 
			BiFunction<Locale, TranslationHint, String> translate) {
		return new StringFirstTranslator(getDefaultLocale, translate);
	}
	
	static StringFirstTranslator of(
			BiFunction<Locale, TranslationHint, String> translate) {
		return of(Locale::getDefault, translate);
	}
	
	class StringFirstTranslator extends AbstractTranslator {

		protected final BiFunction<Locale, TranslationHint, String> translate;

		public StringFirstTranslator(
				final Supplier<Locale> getDefaultLocale, 
				final BiFunction<Locale, TranslationHint, String> translate) {
			super(getDefaultLocale);
			this.translate = requireNonNull(translate);
		}

		@Override
		public String translate(Locale locale, TranslationHint hint) {
			return translate.apply(locale, hint);
		}

		@Override
		public <A extends Appendable> A translate(A appendable, Locale locale, TranslationHint hint) throws IOException {
			appendable.append(translate(locale, hint));
			return appendable;
		}
	}
	
	static AppendableFirstTranslator of(
			Supplier<Locale> getDefaultLocale, 
			AppendingTranslationFunction translate) {
		return new AppendableFirstTranslator(getDefaultLocale, translate);
	}
	
	static AppendableFirstTranslator of(
			AppendingTranslationFunction translate) {
		return of(Locale::getDefault, translate);
	}
	
	@FunctionalInterface
	interface AppendingTranslationFunction {
		
		void translate(Appendable appendable, Locale locale, TranslationHint hint, Translator translator) throws IOException;
		
	}
	
	class AppendableFirstTranslator extends AbstractTranslator {
		
		protected final AppendingTranslationFunction translate;
		
		public AppendableFirstTranslator(
				Supplier<Locale> getDefaultLocale, 
				AppendingTranslationFunction translate) {
			super(getDefaultLocale);
			this.translate = requireNonNull(translate);
		}

		@Override
		public String translate(Locale locale, TranslationHint hint) {
			return translate(new StringBuilder(), locale, hint).toString();
		}

		@Override
		public <A extends Appendable> A translate(A appendable, Locale locale, TranslationHint hint) throws IOException {
			translate.translate(appendable, locale, hint, this);
			return appendable;
		}
		
	}

	static Translator of(
			final MessageLookup messageLookup,
			final Format format) {
		return of(Locale::getDefault, messageLookup, format);
	}
	
	static Translator of(
			final Supplier<Locale> getDefaultLocale,
			final MessageLookup messageLookup,
			final Format format) {
		requireNonNull(getDefaultLocale);
		requireNonNull(messageLookup);
		requireNonNull(format);
		return of(getDefaultLocale, (appendable, locale, hint, translator) -> {
			String message = messageLookup.getMessage(locale, hint.getKey(), hint.getFallback());
			Formatable formatable = format.toFormatable(locale, message);
			formatable.format(appendable, hint.getArguments(), translator);
		});
	}
	
}
