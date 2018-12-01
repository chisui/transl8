package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

import static com.github.chisui.translate.ObjectUtils.toArrayUnsafe;
import static java.util.Objects.requireNonNull;

public final class MessageFormatFormatter<A> implements Formatter<A, String> {

    private final String message;

    public MessageFormatFormatter(String message) {
        this.message = requireNonNull(message, "message");
    }

    @Override
    @SuppressWarnings({
            "unchecked", "rawtype",
    })
    public String apply(A args, Locale locale, TranslationFunction<String> translator) {
        return new MessageFormat(message, locale)
                .format(Arrays.stream(toArrayUnsafe(args))
                        .map(arg -> translateIfPossible(arg, translator))
                        .toArray());
    }

    private static Object translateIfPossible(Object arg, TranslationFunction<String> translator) {
        if (arg instanceof Translatable) {
            return translator.apply((Translatable) arg);
        } else if (arg instanceof TranslationRequest) {
            return translator.apply((TranslationRequest) arg);
        } else {
            return arg;
        }
    }

    @Override
    public boolean acceptsArgumentsOfType(Type type) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageFormatFormatter<?> that = (MessageFormatFormatter<?>) o;
        return message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }

    @Override
    public String toString() {
        return "MessageFormatFormatter(\"" + message + "\")";
    }
}
