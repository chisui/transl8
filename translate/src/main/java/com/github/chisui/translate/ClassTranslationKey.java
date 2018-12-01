package com.github.chisui.translate;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;


/**
 * A {@link TranslationKey} that represents a Translatable type.
 *
 * @param <C> concrete {@link Translatable} subtype.
 */
public final class ClassTranslationKey<C extends Translatable> implements TranslationKey<ClassTranslationKey<C>, C> {

    private final Class<C> cls;

    ClassTranslationKey(Class<C> cls) {
        this.cls = requireNonNull(cls, "cls");
    }

    @Override
    public Type argType() {
        return cls;
    }

    @Override
    public String toKeyString() {
        return DefaultKeyToString.defaultToKeyString(this);
    }

    public Class<C> cls() {
        return cls;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ClassTranslationKey) {
            return cls.equals(((ClassTranslationKey) o).cls);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return cls.hashCode();
    }
}
