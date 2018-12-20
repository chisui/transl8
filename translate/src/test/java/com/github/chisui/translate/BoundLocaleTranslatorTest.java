package com.github.chisui.translate;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumIndirect.SOME_CONST;
import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumKey.HELLO;
import static nl.jqno.equalsverifier.Warning.NULL_FIELDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class BoundLocaleTranslatorTest {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock Supplier<Locale> locale;
    @Mock Translator<String> translator;

    private BoundLocaleTranslator<String> classUnderTest;

    @Before
    public void setUp() {
        when(locale.get()).thenReturn(Locale.ENGLISH);
        classUnderTest = BoundLocaleTranslator.of(translator, locale);
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(BoundLocaleTranslator.class)
                .usingGetClass()
                .suppress(NULL_FIELDS)
                .verify();
    }

    public Object[][] actions() {
        TestSelfTranslatable translatable = new TestSelfTranslatable();
        String[] args = {"hello", "world"};
        return new Object[][] {
                action(t -> t.apply(TranslationRequest.of(HELLO, args)),
                        t -> t.apply(Locale.ENGLISH, (TranslationKey) HELLO, (Object) args)),
                action(t -> t.apply(translatable),
                        t -> t.apply(Locale.ENGLISH, TranslationKey.of(translatable), translatable)),
                action(t -> t.apply(SOME_CONST),
                        t -> t.apply(Locale.ENGLISH, SOME_CONST, null)),
                action(t -> t.apply(HELLO, "hello", "world"),
                        t -> t.apply(eq(Locale.ENGLISH), (TranslationKey) eq(HELLO), (Object) any(String[].class))),
                action(t -> t.apply(HELLO, args),
                        t -> t.apply(eq(Locale.ENGLISH), (TranslationKey) eq(HELLO), (Object) any(String[].class))),
        };
    }

    private Object[] action(Function<TranslationFunction<String>, String> action, Function<Translator<String>, String> delegatesTo) {
        return new Object[] { action, delegatesTo };
    }

    @Test
    @Parameters(method = "actions")
    public void testDelegate(Function<TranslationFunction<String>, String> action, Function<Translator<String>, String> delegatesTo) {
        when(delegatesTo.apply(translator)).thenReturn("someString");

        assertThat(action.apply(classUnderTest))
                .isEqualTo("someString");

        delegatesTo.apply(verify(translator));
    }

    @Test
    public void testToString() {
        assertThat(BoundLocaleTranslator.of(
                new Translator<String>() {
                    @Override
                    public <K extends TranslationKey<K, A>, A> String apply(Locale locale, K key, A arg) {
                        return null;
                    }

                    public String toString() {
                        return "myTranslator";
                    }
                },
                new Supplier<Locale>() {
                    @Override
                    public Locale get() {
                        return null;
                    }

                    public String toString() {
                        return "myLocale";
                    }
                }
        ).toString())
                .isEqualTo("BoundLocaleTranslator{parentTranslator=myTranslator, locale=myLocale}");
    }
}