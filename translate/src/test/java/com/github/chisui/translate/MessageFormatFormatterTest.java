package com.github.chisui.translate;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Locale;

import static com.github.chisui.translate.TranslationFunction.ofUnsafe;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static nl.jqno.equalsverifier.Warning.NULL_FIELDS;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class MessageFormatFormatterTest {

    final TranslationFunction<String> neverInvoced = ofUnsafe((key, arg) -> {
        throw new IllegalArgumentException("should never be invoced but was with args: " + key + " " + arg);
    });

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(MessageFormatFormatter.class)
                .usingGetClass()
                .suppress(NULL_FIELDS)
                .verify();
    }

    Object[][] args() {
        return new Object[][]{
                {"{0} {1,number,#.00} {2}", "a 12.00 b", new Object[]{"a", 12, "b"}},
                {"{0} {1,number,#.00} {2}", "a 12.00 b", asList("a", 12, "b")},
                {"{0} {1,number,#.00} {2}", "a 12.00 b", ((Iterable<Object>) Arrays.<Object>asList("a", 12, "b")::iterator)},
                {"{0} {1}", "1 2", new byte[]{1, 2}},
                {"{0} {1,number,#.00} {2}", "1 12.00 2", new short[]{1, 12, 2}},
                {"{0} {1,number,#.00} {2}", "1 12.00 2", new int[]{1, 12, 2}},
                {"{0} {1,number,#.00} {2}", "1 12.00 2", new long[]{1, 12, 2}},
                {"{0} {1}", "a b", new char[]{'a', 'b'}},
                {"{0} {1}", "0 1", new float[]{0, 1}},
                {"{0} {1}", "0 1", new double[]{0, 1}},
                {"{0} {1}", "true false", new boolean[]{true, false}},
        };
    }

    @Test
    @Parameters(method = "args")
    public void testApply(String format, String expected, Object obj) {
        assertEquals(expected, MessageFormatFormatter.unsafeOf(format, Locale.ENGLISH)
                .apply(neverInvoced, obj));
    }

    @Test
    @Parameters(method = "args")
    public void testAcceptsArgumentsOfType(String unused0, String unused1, Object arg) {
        assertTrue(MessageFormatFormatter.unsafeOf("", Locale.ENGLISH).acceptsArgumentsOfType(arg.getClass()));
    }

    @Test
    public void testAcceptsArgumentsOfTypeVoid() {
        assertTrue(MessageFormatFormatter.unsafeOf("", Locale.ENGLISH).acceptsArgumentsOfType(Void.class));
    }

    @Test
    public void testInvocesTranslatorKey() {
        String actual = MessageFormatFormatter.ofIterable("{0}", Locale.ENGLISH)
                .apply(ofUnsafe((key, arg) -> {
                            assertEquals(ExampleKeyEnum.KEY, key);
                            assertArrayEquals(new String[]{"hello"}, (String[]) arg);
                            return "world";
                        }),
                        singletonList(TranslationRequest.of(ExampleKeyEnum.KEY, "hello")));
        assertEquals("world", actual);
    }

    @Test
    public void testInvocesTranslatorTranslatable() {
        String actual = MessageFormatFormatter.ofIterable("{0}", Locale.ENGLISH)
                .apply(ofUnsafe((key, arg) -> {
                            assertEquals(TranslationKey.of(ExampleTranslatable.class), key);
                            assertTrue(arg instanceof ExampleTranslatable);
                            return "world";
                        }),
                        singletonList(new ExampleTranslatable()));
        assertEquals("world", actual);
    }

    @Test
    public void testToString() {
        assertEquals("MessageFormatFormatter(en:\"{0} {1,number,#.00} {0}\")",
                MessageFormatFormatter.unsafeOf("{0} {1,number,#.00} {0}", Locale.ENGLISH).toString());
    }

    @Test
    public void testOfGenericArrayCompiles() {
        MessageFormatFormatter.ofGenericArray("", Locale.ENGLISH)
                .apply(neverInvoced, new Object[0]);
    }

    @Test
    public void testOfVoidCompiles() {
        MessageFormatFormatter.ofVoid("", Locale.ENGLISH)
                .apply(neverInvoced,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfVoidMessageHasArgs() {
        MessageFormatFormatter.ofVoid("{0}", Locale.ENGLISH);
    }

    @Test
    public void testOfLongArrayCompiles() {
        MessageFormatFormatter.ofLongArray("", Locale.ENGLISH)
                .apply(neverInvoced, new long[0]);
    }

    @Test
    public void testOfIntArrayCompiles() {
        MessageFormatFormatter.ofIntArray("", Locale.ENGLISH)
                .apply(neverInvoced, new int[0]);
    }

    @Test
    public void testOfShortArrayCompiles() {
        MessageFormatFormatter.ofShortArray("", Locale.ENGLISH)
                .apply(neverInvoced, new short[0]);
    }

    @Test
    public void testOfCharArrayCompiles() {
        MessageFormatFormatter.ofCharArray("", Locale.ENGLISH)
                .apply(neverInvoced, new char[0]);
    }

    @Test
    public void testOfByteArrayCompiles() {
        MessageFormatFormatter.ofByteArray("", Locale.ENGLISH)
                .apply(neverInvoced, new byte[0]);
    }

    @Test
    public void testOfBooleanArrayCompiles() {
        MessageFormatFormatter.ofBooleanArray("", Locale.ENGLISH)
                .apply(neverInvoced, new boolean[0]);
    }

    @Test
    public void testOfDoubleArrayCompiles() {
        MessageFormatFormatter.ofDoubleArray("", Locale.ENGLISH)
                .apply(neverInvoced, new double[0]);
    }

    @Test
    public void testOfFloatArrayCompiles() {
        MessageFormatFormatter.ofFloatArray("", Locale.ENGLISH)
                .apply(neverInvoced, new float[0]);
    }
}