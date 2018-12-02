package com.github.chisui.translate;

import org.junit.Test;

import java.util.Locale;

import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumWithOverride.SOME_CONST;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TranslatorTest {

    private static final TranslationRequest<ExampleKeyEnum, String[]> REQ =
            TranslationRequest.of(ExampleKeyEnum.KEY, "hello");
    private static final ExampleTranslatable TRANSLATABLE = new ExampleTranslatable();

    private Object[] capture;
    private Translator<String> translator = Translator.ofUnsafe((l, k, a) -> {
        capture = new Object[]{l, k, a};
        return "hello";
    });

    @Test
    public void testOf() {
        Translator<String> translator = Translator.<String, String>of(
                ResourceBundleMessageSource.of("testBundle"),
                MessageFormatFormatter::<Object>unsafeOf);

        String actual = translator.apply(ExampleKeyEnum.KEY, "world", "Mars");
        assertEquals("hello world and Mars", actual);
    }

    @Test
    public void testBindLocaleBindsLocale() {
        withDefaultLocale(Locale.GERMAN, () -> {
            Translator<String> boundTranslator = translator.bindLocale(Locale.ENGLISH);

            assertEquals(Locale.GERMAN, translator.defaultLocale());
            assertEquals(Locale.ENGLISH, boundTranslator.defaultLocale());
        });
    }

    @Test
    public void testBindLocaleUsesBoundLocale() {
        withDefaultLocale(Locale.GERMAN, () -> {
            translator.bindLocale(Locale.ENGLISH)
                    .apply(TRANSLATABLE);

            assertArrayEquals(new Object[]{
                    Locale.ENGLISH,
                    TranslationKey.of(ExampleTranslatable.class),
                    TRANSLATABLE,
            }, capture);
        });
    }

    @Test
    public void testBindLocaleCanBindLocaleAgain() {
        withDefaultLocale(Locale.GERMAN, () -> {
            translator.bindLocale(Locale.ENGLISH)
                    .bindLocale(Locale.FRENCH)
                    .apply(TRANSLATABLE);

            assertArrayEquals(new Object[]{
                    Locale.FRENCH,
                    TranslationKey.of(ExampleTranslatable.class),
                    TRANSLATABLE,
            }, capture);
        });
    }

    @Test
    public void testApplyLocaleRequest() {
        translator.apply(Locale.GERMAN, REQ);
        assertArrayEquals(new Object[]{
                Locale.GERMAN,
                REQ.key(),
                REQ.arg(),
        }, capture);
    }

    @Test
    public void testApplyLocaleTranslatable() {
        translator.apply(Locale.GERMAN, TRANSLATABLE);
        assertArrayEquals(new Object[]{
                Locale.GERMAN,
                TranslationKey.of(TRANSLATABLE),
                TRANSLATABLE,
        }, capture);
    }

    @Test
    public void testApplyLocaleKeyVoid() {
        translator.apply(Locale.GERMAN, SOME_CONST);
        assertArrayEquals(new Object[]{
                Locale.GERMAN,
                SOME_CONST,
                null,
        }, capture);
    }

    @Test
    public void testApplyLocaleKeyVarArgs() {
        translator.apply(Locale.GERMAN, ExampleKeyEnum.KEY, "hello", "world");
        assertEquals(Locale.GERMAN, capture[0]);
        assertEquals(ExampleKeyEnum.KEY, capture[1]);
        assertArrayEquals(new String[]{"hello", "world"}, (Object[]) capture[2]);
    }

    enum SomeEnum implements EnumTranslationKey<SomeEnum, Long> {KEY}

    @Test
    public void testApplyLocaleKeyArg() {
        translator.apply(Locale.GERMAN, SomeEnum.KEY, 42L);
        assertArrayEquals(new Object[]{
                Locale.GERMAN,
                SomeEnum.KEY,
                42L,
        }, capture);
    }

    @Test
    public void testApplyRequest() {
        withDefaultLocale(Locale.GERMAN, () -> translator.apply(REQ));
        assertArrayEquals(new Object[]{
                Locale.GERMAN,
                REQ.key(),
                REQ.arg(),
        }, capture);
    }

    @Test
    public void testApplyTranslatable() {
        withDefaultLocale(Locale.GERMAN, () -> translator.apply(TRANSLATABLE));
        assertArrayEquals(new Object[]{
                Locale.GERMAN,
                TranslationKey.of(TRANSLATABLE),
                TRANSLATABLE,
        }, capture);
    }

    @Test
    public void testApplyKeyVoid() {
        withDefaultLocale(Locale.GERMAN, () -> translator.apply(SOME_CONST));
        assertArrayEquals(new Object[]{
                Locale.GERMAN,
                SOME_CONST,
                null,
        }, capture);
    }

    @Test
    public void testApplyKeyVarArgs() {
        withDefaultLocale(Locale.GERMAN, () -> translator.apply(ExampleKeyEnum.KEY, "hello", "world"));
        assertEquals(Locale.GERMAN, capture[0]);
        assertEquals(ExampleKeyEnum.KEY, capture[1]);
        assertArrayEquals(new String[]{"hello", "world"}, (Object[]) capture[2]);
    }

    @Test
    public void testApplyKeyArg() {
        withDefaultLocale(Locale.GERMAN, () -> translator.apply(SomeEnum.KEY, 42L));
        assertArrayEquals(new Object[]{
                Locale.GERMAN,
                SomeEnum.KEY,
                42L,
        }, capture);
    }

    private void withDefaultLocale(Locale locale, Runnable action) {
        Locale oldDefault = Locale.getDefault();
        try {
            Locale.setDefault(locale);
            action.run();
        } finally {
            Locale.setDefault(oldDefault);
        }
    }
}
