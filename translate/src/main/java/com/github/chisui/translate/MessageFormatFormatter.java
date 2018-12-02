package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

import static com.github.chisui.translate.ObjectUtils.toArrayUnsafe;
import static com.github.chisui.translate.ObjectUtils.toClass;
import static java.util.Objects.requireNonNull;

public final class MessageFormatFormatter<A> implements Formatter<A, String> {

    private final MessageFormat messageFormat;

    private MessageFormatFormatter(String message, Locale locale) {
        this.messageFormat = new MessageFormat(message, locale);
    }

    public static <A> MessageFormatFormatter<A> unsafeOf(String message, Locale locale) {
        return new MessageFormatFormatter<>(message, locale);
    }

    public static <A extends Iterable<?>> MessageFormatFormatter<A> ofIterable(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    public static MessageFormatFormatter<Void> ofVoid(String message, Locale locale) {
        if (new MessageFormat(message).getFormats().length != 0) {
            throw new IllegalArgumentException("expected message to not require any arguments \"" + message + "\"");
        }
        return unsafeOf(message, locale);
    }

    public static <A> MessageFormatFormatter<A[]> ofGenericArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    public static MessageFormatFormatter<long[]> ofLongArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    public static MessageFormatFormatter<int[]> ofIntArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    public static MessageFormatFormatter<short[]> ofShortArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    public static MessageFormatFormatter<char[]> ofCharArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    public static MessageFormatFormatter<byte[]> ofByteArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    public static MessageFormatFormatter<boolean[]> ofBooleanArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    public static MessageFormatFormatter<double[]> ofDoubleArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    public static MessageFormatFormatter<float[]> ofFloatArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    @Override
    @SuppressWarnings({
            "unchecked", "rawtype",
    })
    public String apply(A args, TranslationFunction<String> translator) {
        if (args == null && messageFormat.getFormats().length == 0) {
            return messageFormat.format(new Object[0]);
        } else {
            return messageFormat.format(Arrays.stream(toArrayUnsafe(args))
                    .map(arg -> translateIfPossible(arg, translator))
                    .toArray());
        }
    }

    @SuppressWarnings("unchecked")
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
        Class<?> cls = toClass(type);
        return cls.isArray()
                || Iterable.class.isAssignableFrom(cls)
                || (messageFormat.getFormats().length == 0 && Void.class.equals(cls));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageFormatFormatter<?> that = (MessageFormatFormatter<?>) o;
        return messageFormat.equals(that.messageFormat);
    }

    @Override
    public int hashCode() {
        return messageFormat.hashCode();
    }

    @Override
    public String toString() {
        return "MessageFormatFormatter(" + messageFormat.getLocale() + ":\"" + messageFormat.toPattern() + "\")";
    }
}
