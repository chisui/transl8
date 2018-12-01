package com.github.chisui.translate;

import java.lang.reflect.Type;
import java.util.Locale;

public interface Formatter<A, R> {

    boolean acceptsArgumentsOfType(Type type);

    R apply(A arg, Locale locale, TranslationFunction<R> translator);

}
