package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class PropertyGetter<A, B> implements Getter<A, B> {

    private final String prop;

    PropertyGetter(String prop) {
        this.prop = requireNonNull(prop, "PropertyGetter.prop");
    }

    public Optional<Property<A>> prop(Type t) {
        return ObjectUtils.property(t, prop);
    }

    @Override
    @SuppressWarnings({
            "unchecked" // can't do anything about it :(
    })
    public B apply(A a) {
        return (B) prop(a.getClass())
                .orElseThrow(() -> new IllegalArgumentException("could not find property \"" + prop + "\" on " + a.getClass()))
                .apply(a);
    }

    @Override
    public boolean acceptsArgumentsOfType(Type type) {
        return prop(type).isPresent();
    }
}
