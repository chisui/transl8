package net.chisui.transl8.lookup;

import static java.util.Objects.requireNonNull;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public interface MessageLookup {

	String getMessage(Locale locale, Object key, Optional<String> fallback);

	public static MessageLookup fallback() {
		return of((locale, key) -> Optional.empty());
	}

	public static MessageLookup of(final MessageSource messageSource) {
		return of(String::valueOf, messageSource);
	}

	public static MessageLookup of(
			final Function<Object, String> toString,
			final MessageSource messageSource) {
		return of(toString, messageSource, key -> "???" + key + "???");
	}

	public static MessageLookup of(
			final MessageSource messageSource,
			final Function<String, String> globalFallback) {
		return of(String::valueOf, messageSource, globalFallback);
	}

	public static MessageLookup of(
			final Function<Object, String> toString,
			final MessageSource messageSource,
			final Function<String, String> globalFallback) {
		return new ComposedMessageLookup(toString, messageSource, globalFallback);
	}

	class ComposedMessageLookup implements MessageLookup {

		private final Function<Object, String> toString;
		private final MessageSource messageSource;
		private final Function<String, String> globalFallback;

		private ComposedMessageLookup(
				final Function<Object, String> toString,
				final MessageSource messageSource,
				final Function<String, String> globalFallback) {
			this.toString = requireNonNull(toString, "toString may not be null");
			this.messageSource = requireNonNull(messageSource, "Properties Supplier may not be null");
			this.globalFallback = requireNonNull(globalFallback, "global fallback may not be null");
		}

		@Override
		public String getMessage(Locale locale, Object key, Optional<String> fallback) {
			String stringKey = toString.apply(key);
			Optional<String> msg = messageSource.findMessage(locale, stringKey);
			if (msg.isPresent()) {
				return msg.get();
			} else if (fallback.isPresent()) {
				return fallback.get();
			} else {
				return globalFallback.apply(stringKey);
			}
		}
	}
}
