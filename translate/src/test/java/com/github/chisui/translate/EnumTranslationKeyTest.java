package com.github.chisui.translate;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EnumTranslationKeyTest {

    enum TestEnumKey implements EnumTranslationKey<TestEnumKey, String[]> {
        HELLO,
        @TranslationOverride("override")
        GOODBYE,
    }

    @TranslationOverride("enum")
    enum TestEnumWithOverride implements EnumTranslationKey<TestEnumWithOverride, Void> {
        SOME_CONST
    }

    @TranslationOverride(prefix = "super")
    interface SuperEnumTranslationKey<E extends Enum<E> & SuperEnumTranslationKey<E>> extends EnumTranslationKey<E, Void> {

    }

    enum TestEnumIndirect implements SuperEnumTranslationKey<TestEnumIndirect> {
        SOME_CONST
    }

    @TranslationOverride(prefix = "pre")
    enum TestEnumWithOverridePrefix implements EnumTranslationKey<TestEnumWithOverride, Void> {
        SOME_CONST
    }

    @Test
    public void testEnumImpl() {
        assertThat(TestEnumKey.HELLO.toKeyString())
            .isEqualTo("com.github.chisui.translate.EnumTranslationKeyTest.TestEnumKey.HELLO");
    }

    @Test
    public void testEnumOverrideConst() {
        assertThat(TestEnumKey.GOODBYE.toKeyString())
            .isEqualTo("com.github.chisui.translate.EnumTranslationKeyTest.TestEnumKey.override");
    }

    @Test
    public void testEnumOverride() {
        assertThat(TestEnumWithOverride.SOME_CONST.toKeyString())
                .isEqualTo("enum.SOME_CONST");
    }

    @Test
    public void testEnumOverridePrefix() {
        assertThat(TestEnumWithOverridePrefix.SOME_CONST.toKeyString())
                .isEqualTo("pre.TestEnumWithOverridePrefix.SOME_CONST");
    }

    @Test
    public void testEnumOverrideIndirect() {
        assertThat(TestEnumIndirect.SOME_CONST.toKeyString())
                .isEqualTo("super.TestEnumIndirect.SOME_CONST");
    }

    @Test
    public void testArgTypeDirect() {
        assertThat(TestEnumKey.HELLO.argType())
                .isEqualTo(String[].class);
    }

    @Test
    public void testArgTypeInirect() {
        assertThat(TestEnumIndirect.SOME_CONST.argType())
                .isEqualTo(Void.class);
    }
}
