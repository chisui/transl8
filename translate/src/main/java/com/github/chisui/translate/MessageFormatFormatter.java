package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

import static com.github.chisui.translate.ObjectUtils.toArrayUnsafe;
import static com.github.chisui.translate.ObjectUtils.toClass;
import static java.util.Objects.requireNonNull;

public final class MessageFormatFormatter<A> implements Formatter<A, String> {

    private final String message;

    private MessageFormatFormatter(String message) {
        this.message = requireNonNull(message, "message");
    }

    public static <A> MessageFormatFormatter<A> unsafeOf(String message) {
        return new MessageFormatFormatter<>(message);
    }

    public static <A extends Iterable<?>> MessageFormatFormatter<A> ofIterable(String message) {
        return unsafeOf(message);
    }

    public static MessageFormatFormatter<Void> ofVoid(String message) {
        if (new MessageFormat(message).getFormats().length != 0) {
            throw new IllegalArgumentException("expected message to not require any arguments \"" + message + "\"");
        }
        return unsafeOf(message);
    }

    public static <A> MessageFormatFormatter<A[]> ofGenericArray(String message) {
        return unsafeOf(message);
    }

    public static MessageFormatFormatter<long[]> ofLongArray(String message) {
        return unsafeOf(message);
    }

    public static MessageFormatFormatter<int[]> ofIntArray(String message) {
        return unsafeOf(message);
    }

    public static MessageFormatFormatter<short[]> ofShortArray(String message) {
        return unsafeOf(message);
    }

    public static MessageFormatFormatter<char[]> ofCharArray(String message) {
        return unsafeOf(message);
    }

    public static MessageFormatFormatter<byte[]> ofByteArray(String message) {
        return unsafeOf(message);
    }

    public static MessageFormatFormatter<boolean[]> ofBooleanArray(String message) {
        return unsafeOf(message);
    }

    public static MessageFormatFormatter<double[]> ofDoubleArray(String message) {
        return unsafeOf(message);
    }

    public static MessageFormatFormatter<float[]> ofFloatArray(String message) {
        return unsafeOf(message);
    }

    @Override
    @SuppressWarnings({
            "unchecked", "rawtype",
    })
    public String apply(A args, Locale locale, TranslationFunction<String> translator) {
        MessageFormat format = new MessageFormat(message, locale);
        if (args == null && format.getFormats().length == 0) {
            return format.format(new Object[0]);
        } else {
            return format.format(Arrays.stream(toArrayUnsafe(args))
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
                || (new MessageFormat(message).getFormats().length == 0 && Void.class.equals(cls));
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
