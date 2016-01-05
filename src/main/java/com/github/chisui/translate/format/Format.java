package com.github.chisui.translate.format;

import java.text.MessageFormat;
import java.util.Formatter;
import java.util.Locale;

@FunctionalInterface
public interface Format {

	Formatable toFormatable(Locale locale, String message);
	
	static Format ofFormatter() {
		return (locale, format) -> (appendable, args, translator) ->
				new Formatter(appendable, locale).format(format, args.stream()
						.map(arg -> translator.translate(locale, arg))
						.toArray());
	}
	
	static Format ofMessageFormat() {
		return (locale, pattern) -> {
			MessageFormat format = new MessageFormat(pattern, locale);
			return (appendable, args, translator) -> appendable.append(format.format(args.stream()
							.map(arg -> translator.translate(locale, arg))
							.toArray()));
		};
	}
	
}
