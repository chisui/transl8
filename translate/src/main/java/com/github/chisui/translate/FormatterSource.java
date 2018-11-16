package com.github.chisui.translate;

import java.util.function.Function;

public interface FormatterSource<R> {

    <K extends TranslationKey<K, A>, A> Function<A, R> formatterOf(K key);

}
