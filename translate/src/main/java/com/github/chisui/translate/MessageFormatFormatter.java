package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;

import static com.github.chisui.translate.ObjectUtils.toArrayUnsafe;
import static com.github.chisui.translate.ObjectUtils.toClass;
import static java.util.Objects.requireNonNull;

public final class MessageFormatFormatter<A> implements Formatter<A, String> {

    private final MessageFormat mf;

    public MessageFormatFormatter(MessageFormat messageFormat) {
        this.mf = requireNonNull(messageFormat, "messageFormat");
    }

    @Override
    @SuppressWarnings({
            "unchecked", "rawtype",
    })
    public String apply(A o, TranslationFunction<String> translator) {
        Object[] args = Arrays.stream(toArrayUnsafe(o))
                .map(arg -> {
                    if (arg instanceof Translatable) {
                        return translator.apply((Translatable) arg);
                    } else if (arg instanceof TranslationRequest) {
                        return translator.apply((TranslationRequest) arg);
                    } else {
                        return arg;
                    }
                })
                .toArray();
        return mf.format(args);
    }

    @Override
    public boolean acceptsArgumentsOfType(Type type) {
        Class<?> cls = toClass(type);
        return cls.isArray() || Iterable.class.isAssignableFrom(cls);
    }

}
