package com.github.chisui.translate;

import org.junit.Test;

import java.util.Locale;

import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumWithOverride.SOME_CONST;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
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
                MessageFormatFormatter::unsafeOf);

        String actual = translator.apply(ExampleKeyEnum.KEY, "world", "Mars");
        assertEquals("hello world and Mars", actual);
    }

    @Test
    public void testBindLocaleBindsLocale() {
        withDefaultLocale(GERMAN, () -> {
            Translator<String> boundTranslator = translator.bindLocale(ENGLISH);

            assertEquals(GERMAN, translator.defaultLocale());
            assertEquals(ENGLISH, boundTranslator.defaultLocale());
        });
    }

    @Test
    public void testBindLocaleUsesBoundLocale() {
        withDefaultLocale(GERMAN, () -> {
            translator.bindLocale(ENGLISH)
                    .apply(TRANSLATABLE);

            assertArrayEquals(new Object[]{
                    ENGLISH,
                    TranslationKey.of(ExampleTranslatable.class),
                    TRANSLATABLE,
            }, capture);
        });
    }

    @Test
    public void testBindLocaleCanBindLocaleAgain() {
        withDefaultLocale(GERMAN, () -> {
            translator.bindLocale(ENGLISH)
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
        translator.apply(GERMAN, REQ);
        assertArrayEquals(new Object[]{
                GERMAN,
                REQ.key(),
                REQ.arg(),
        }, capture);
    }

    @Test
    public void testApplyLocaleTranslatable() {
        translator.apply(GERMAN, TRANSLATABLE);
        assertArrayEquals(new Object[]{
                GERMAN,
                TranslationKey.of(TRANSLATABLE),
                TRANSLATABLE,
        }, capture);
    }

    @Test
    public void testApplyLocaleKeyVoid() {
        translator.apply(GERMAN, SOME_CONST);
        assertArrayEquals(new Object[]{
                GERMAN,
                SOME_CONST,
                null,
        }, capture);
    }

    @Test
    public void testApplyLocaleKeyVarArgs() {
        translator.apply(GERMAN, ExampleKeyEnum.KEY, "hello", "world");
        assertEquals(GERMAN, capture[0]);
        assertEquals(ExampleKeyEnum.KEY, capture[1]);
        assertArrayEquals(new String[]{"hello", "world"}, (Object[]) capture[2]);
    }

    enum SomeEnum implements EnumTranslationKey<SomeEnum, Long> {KEY}

    @Test
    public void testApplyLocaleKeyArg() {
        translator.apply(GERMAN, SomeEnum.KEY, 42L);
        assertArrayEquals(new Object[]{
                GERMAN,
                SomeEnum.KEY,
                42L,
        }, capture);
    }

    @Test
    public void testApplyRequest() {
        withDefaultLocale(GERMAN, () -> translator.apply(REQ));
        assertArrayEquals(new Object[]{
                GERMAN,
                REQ.key(),
                REQ.arg(),
        }, capture);
    }

    @Test
    public void testApplyTranslatable() {
        withDefaultLocale(GERMAN, () -> translator.apply(TRANSLATABLE));
        assertArrayEquals(new Object[]{
                GERMAN,
                TranslationKey.of(TRANSLATABLE),
                TRANSLATABLE,
        }, capture);
    }

    @Test
    public void testApplyKeyVoid() {
        withDefaultLocale(GERMAN, () -> translator.apply(SOME_CONST));
        assertArrayEquals(new Object[]{
                GERMAN,
                SOME_CONST,
                null,
        }, capture);
    }

    @Test
    public void testApplyKeyVarArgs() {
        withDefaultLocale(GERMAN, () -> translator.apply(ExampleKeyEnum.KEY, "hello", "world"));
        assertEquals(GERMAN, capture[0]);
        assertEquals(ExampleKeyEnum.KEY, capture[1]);
        assertArrayEquals(new String[]{"hello", "world"}, (Object[]) capture[2]);
    }

    @Test
    public void testApplyKeyArg() {
        withDefaultLocale(GERMAN, () -> translator.apply(SomeEnum.KEY, 42L));
        assertArrayEquals(new Object[]{
                GERMAN,
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
