package net.chisui.transl8;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.chisui.transl8.format.Format;
import net.chisui.transl8.lookup.MessageLookup;

public class IT {

	@Test
	public void testAll() throws Exception {

		Translatable t = () -> TranslationHint.of("my.key")
				.withArgument((Translatable) () -> TranslationHint.of("sub.key").withFallback("hi"))
				.andArguments("arg1", new Object() {
					public String toString() {
						return "arg2";
					}
				}).withFallback("fallback {0}, {1}, {2}");

		Translator translator = Translator.of(
				MessageLookup.fallback(), 
				Format.ofMessageFormat());

		assertEquals("fallback hi, arg1, arg2", translator.translate(t));
	}

}
