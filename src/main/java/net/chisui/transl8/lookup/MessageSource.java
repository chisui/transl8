package net.chisui.transl8.lookup;

import static java.util.Objects.requireNonNull;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Factory for message source {@link Function Functions}. 
 */
@FunctionalInterface
public interface MessageSource {

	Optional<String> findMessage(Locale locale, String key);

	public static MessageSource ofResourceBundle(final String baseName) {
		requireNonNull(baseName);
		return (locale, key) -> {
			try {
				return Optional.ofNullable(ResourceBundle.getBundle(baseName, locale).getString(key));
			} catch (MissingResourceException e) {
				return Optional.empty();
			}
		};
	}
	
	public static MessageSource ofMap(
			 final Function<? super Locale, ? extends Map<? super String, String>> getMap) {
		requireNonNull(getMap);
		return (locale, key) -> Optional.ofNullable(getMap.apply(locale).get(key));
	}
}
