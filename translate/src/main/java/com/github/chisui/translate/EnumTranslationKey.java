package com.github.chisui.translate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static com.github.chisui.translate.DefaultKeyToString.defaultToKeyString;
import static java.util.Arrays.asList;

public interface EnumTranslationKey<
    SELF extends Enum<SELF> & EnumTranslationKey<SELF, A>,
    A> extends TranslationKey<SELF, A> {

    default Type argType() {
        Queue<Type> clss = new LinkedList<>(asList(getClass().getGenericInterfaces()));
        while (!clss.isEmpty()) {
            Type t = clss.poll();
            if (t instanceof ParameterizedType) {
                ParameterizedType p = (ParameterizedType) t;
                if (EnumTranslationKey.class.equals(p.getRawType())) {
                    return p.getActualTypeArguments()[1];
                } else {
                    Arrays.stream(((Class<?>) p.getRawType()).getGenericInterfaces())
                        .forEach(clss::offer);
                }
            } else {
                break;
            }
        }
        throw new IllegalStateException(getClass() + " implements EnumTranslationKey wrong.");
    }

    default String toKeyString() {
        return defaultToKeyString(this);
    }
}
