package com.github.chisui.translate;

import java.util.Optional;
import java.util.stream.Stream;

public interface TranslationKeyBase<SELF extends TranslationKeyBase<SELF>> {

    default String toKeyString() {
        return toKeyString(getClass());
    }

    static <A extends TranslationKeyBase<A>> String toKeyString(Class<? extends A> cls) {
        return Optional.ofNullable(cls.getAnnotation(TranslationOverride.class))
                .flatMap(to -> {
                    Optional<String> override = Stream.of(to.baseKey(), to.value())
                            .filter(s -> !s.isEmpty())
                            .findFirst();
                    Optional<String> customPrefix = Optional.of(to.prefix())
                            .filter(s -> !s.isEmpty())
                            .map(s -> s + "." + cls.getSimpleName());
                    return override
                            .map(Optional::of)
                            .orElse(customPrefix);
                })
                .orElseGet(cls::getCanonicalName);
    }

}
