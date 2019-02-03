package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Locale;

import static com.github.chisui.translate.ObjectUtils.toArrayUnsafe;
import static com.github.chisui.translate.ObjectUtils.toClass;
import static java.util.Arrays.stream;

/**
 * A {@link MessageFormat} backed {@link Formatter}.
 *
 * @param <Arg>
 */
public final class MessageFormatFormatter<Arg> implements Formatter<Arg, String> {

    private static final Object[] NO_ARGS = new Object[0];

    private final MessageFormat messageFormat;

    private MessageFormatFormatter(String message, Locale locale) {
        this.messageFormat = new MessageFormat(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * <strong>
     *     THIS FUNCTION IS UNSAFE.
     *     Since the argument type has to be either an Array, an Iterable or Void, but this function allows the argument type
     *     to be any desired type. If possible use the type specific function instead.
     *     If you have to use it, make sure that the argument type is valid.
     * </strong>
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @param <Arg> expected argument type
     * @return the {@link MessageFormatFormatter}
     * @see #ofIterable(String, Locale)
     * @see #ofVoid(String, Locale)
     * @see #ofGenericArray(String, Locale)
     * @see #ofBooleanArray(String, Locale)
     * @see #ofByteArray(String, Locale)
     * @see #ofCharArray(String, Locale)
     * @see #ofDoubleArray(String, Locale)
     * @see #ofFloatArray(String, Locale)
     * @see #ofIntArray(String, Locale)
     * @see #ofLongArray(String, Locale)
     * @see #ofShortArray(String, Locale)
     */
    public static <Arg> MessageFormatFormatter<Arg> unsafeOf(String message, Locale locale) {
        return new MessageFormatFormatter<>(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @param <Arg> expected argument type
     * @return the {@link MessageFormatFormatter}
     */
    public static <Arg extends Iterable<?>> MessageFormatFormatter<Arg> ofIterable(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} that does not expect any arguments from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @return the {@link MessageFormatFormatter}
     */
    public static MessageFormatFormatter<Void> ofVoid(String message, Locale locale) {
        if (new MessageFormat(message).getFormats().length != 0) {
            throw new IllegalArgumentException("expected message to not require any arguments \"" + message + "\"");
        }
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @param <Arg> expected argument type
     * @return the {@link MessageFormatFormatter}
     */
    public static <Arg> MessageFormatFormatter<Arg[]> ofGenericArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @return the {@link MessageFormatFormatter}
     */
    public static MessageFormatFormatter<long[]> ofLongArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @return the {@link MessageFormatFormatter}
     */
    public static MessageFormatFormatter<int[]> ofIntArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @return the {@link MessageFormatFormatter}
     */
    public static MessageFormatFormatter<short[]> ofShortArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @return the {@link MessageFormatFormatter}
     */
    public static MessageFormatFormatter<char[]> ofCharArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @return the {@link MessageFormatFormatter}
     */
    public static MessageFormatFormatter<byte[]> ofByteArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @return the {@link MessageFormatFormatter}
     */
    public static MessageFormatFormatter<boolean[]> ofBooleanArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @return the {@link MessageFormatFormatter}
     */
    public static MessageFormatFormatter<double[]> ofDoubleArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    /**
     * Creates a {@link MessageFormatFormatter} from a given message and an associated locale.
     *
     * @param message of the {@link MessageFormat}
     * @param locale of the {@link MessageFormat}
     * @return the {@link MessageFormatFormatter}
     */
    public static MessageFormatFormatter<float[]> ofFloatArray(String message, Locale locale) {
        return unsafeOf(message, locale);
    }

    @Override
    @SuppressWarnings({
            "unchecked", "rawtype",
    })
    public String apply(TranslationFunction<String> translator, Arg args) {
        if (args == null && messageFormat.getFormats().length == 0) {
            return messageFormat.format(NO_ARGS);
        } else {
            return messageFormat.format(stream(toArrayUnsafe(args)
                    .orElseThrow(() -> new IllegalArgumentException("could not turn " + args + " into an array.")))
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
        int formatterCount = messageFormat.getFormats().length;
        return cls.isArray()
                || Iterable.class.isAssignableFrom(cls)
                || (formatterCount == 1 && Void.class != cls)
                || (formatterCount == 0 && Void.class == cls);
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
