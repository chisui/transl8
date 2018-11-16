package com.github.chisui.translate;

import static java.util.Objects.requireNonNull;


public final class ClassTranslationKey<C> implements TranslationKey<ClassTranslationKey<C>, C> {

    private final Class<C> cls;

    ClassTranslationKey(Class<C> cls) {
        this.cls = requireNonNull(cls);
    }


    @Override
    public String toKeyString() {
        return cls.getCanonicalName();
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
