package com.github.chisui.translate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.chisui.translate.format.Format;
import com.github.chisui.translate.lookup.MessageLookup;

public class IT {

	@Test
	public void testAll() throws Exception {

		Translatable t = () -> TranslationHint.of("my.key")
				.withArguments((Translatable) () -> TranslationHint.of("sub.key").withFallback("hi"))
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
