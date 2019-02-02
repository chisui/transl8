package com.github.chisui.translate;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Formatter} that always returns a constant value.
 *
 * @param <Arg> Type of argument
 * @param <Return> type of the constant value
 */
public final class ConstFormatter<Arg, Return>
        implements Formatter<Arg, Return> {

    private final Return value;

    ConstFormatter(
            final Return value) {
        this.value = requireNonNull(value, "ConstFormatter.value");
    }

    @Override
    public boolean acceptsArgumentsOfType(
            final Type type) {
        return true;
    }

    @Override
    public Return apply(
            final TranslationFunction<Return> translator,
            final Arg arg) {
        return value;
    }

    public Return value() {
        return value;
    }
}
