package com.github.chisui.translate;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

import com.github.chisui.translate.TranslationHint.FullTranslationHint;
import com.github.chisui.translate.TranslationHint.KeyArgumentsTranslationHint;
import com.github.chisui.translate.TranslationHint.KeyFallbackTranslationHint;
import com.github.chisui.translate.TranslationHint.KeyTranslationHint;

public class TranslationHintTest {
	
	@Test
	public void testGetArguments() throws Exception {
		
		TranslationHint hint = () -> "key";
		
		assertThat(hint.getArguments(), hasSize(0));
	}

	@Test
	public void testGetFallback() throws Exception {
		
		TranslationHint hint = () -> "key";
		
		assertThat(hint.getFallback(), is(Optional.empty()));
	}

	@Test
	public void testOfKeyAndArgs() throws Exception {

		KeyTranslationHint hint = TranslationHint.of("key");
		
		assertThat(hint.getKey(), is("key"));
		assertThat(hint.getArguments(), hasSize(0));
		assertThat(hint.getFallback(), is(Optional.empty()));
		
		assertThat(hint.withArguments("a", "b").getArguments(), contains("a", "b"));
		
		KeyArgumentsTranslationHint hintWithArgs = hint
				.withArguments("arg0")
				.andArguments("arg1","arg2");
		
		assertThat(hintWithArgs.getKey(), is("key"));
		assertThat(hintWithArgs.getArguments(), contains("arg0", "arg1", "arg2"));
		assertThat(hintWithArgs.getFallback(), is(Optional.empty()));
		
		FullTranslationHint fullHint = hintWithArgs.withFallback("fallback");
		
		assertThat(fullHint.getKey(), is("key"));
		assertThat(fullHint.getArguments(), contains("arg0", "arg1", "arg2"));
		assertThat(fullHint.getFallback(), is(Optional.of("fallback")));
	}
	
	@Test
	public void testOfKeyAndFallback() throws Exception {

		KeyTranslationHint hint = TranslationHint.of("key");
		
		assertThat(hint.getKey(), is("key"));
		assertThat(hint.getArguments(), hasSize(0));
		assertThat(hint.getFallback(), is(Optional.empty()));
		
		KeyFallbackTranslationHint hintWithFallback = hint.withFallback("fallback");
				
		assertThat(hintWithFallback.getKey(), is("key"));
		assertThat(hintWithFallback.getArguments(), hasSize(0));
		assertThat(hintWithFallback.getFallback(), is(Optional.of("fallback")));
		
		assertThat(hintWithFallback.withArguments("a", "b").getArguments(), contains("a", "b"));
		
		FullTranslationHint fullHint = hintWithFallback
				.withArguments("arg0")
				.andArguments("arg1","arg2");
		
		assertThat(fullHint.getKey(), is("key"));
		assertThat(fullHint.getArguments(), contains("arg0", "arg1", "arg2"));
		assertThat(fullHint.getFallback(), is(Optional.of("fallback")));
	}

	@Test
	public void testOfObjectObjectArray() throws Exception {

		KeyArgumentsTranslationHint hint = TranslationHint.of("key", "arg0", "arg1");
		
		assertThat(hint.getKey(), is("key"));
		assertThat(hint.getArguments(), contains("arg0", "arg1"));
		assertThat(hint.getFallback(), is(Optional.empty()));
	}
}
