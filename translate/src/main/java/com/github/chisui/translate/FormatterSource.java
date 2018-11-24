package com.github.chisui.translate;

public interface FormatterSource<R> {

    <K extends TranslationKey<K, A>, A> Formatter<A, R> formatterOf(K key);

}
