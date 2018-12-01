package com.github.chisui.translate;

import com.github.chisui.translate.EnumTranslationKeyTest.TestEnumKey;
import org.junit.Test;

import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumIndirect.SOME_CONST;
import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumKey.HELLO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ToTranslationRequestTest implements Translatable {

    @Test
    public void applyTranslationRequest() {
        TranslationRequest<TestEnumKey, String[]> req = TranslationRequest.of(HELLO, new String[]{"a", "b"});
        assertSame(req, ToTranslationRequest.INSTANCE.apply(req));
    }

    @Test
    public void applyTranslatable() {
        assertEquals(
                ToTranslationRequest.INSTANCE.apply(this),
                TranslationRequest.of(
                        new ClassTranslationKey<>(ToTranslationRequestTest.class), this));
    }

    @Test
    public void applyVarArgs() {
        assertEquals(
                ToTranslationRequest.INSTANCE.apply(HELLO, "a", "b"),
                TranslationRequest.of(HELLO, new String[]{"a", "b"}));
    }

    @Test
    public void applyVoid() {
        assertEquals(
                ToTranslationRequest.INSTANCE.apply(SOME_CONST),
                TranslationRequest.of(SOME_CONST, null));
    }

    @Test
    public void applyKeyArg() {
        assertEquals(
                ToTranslationRequest.INSTANCE.apply(
                        new ClassTranslationKey<>(ToTranslationRequestTest.class), this),
                TranslationRequest.of(
                        new ClassTranslationKey<>(ToTranslationRequestTest.class), this));
    }
}