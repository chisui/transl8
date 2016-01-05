package com.github.chisui.translate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.chisui.translate.TranslationHint.ImmutableTranslationHint;

import mockit.Expectations;
import mockit.Mocked;

public class ImmutableTranslationHintTest {
	
	@Rule public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testGetters(
			
		@Mocked Object key,
		@Mocked List<?> args,
		@Mocked Optional<String> fallback
			
	) throws Exception {
		
		new Expectations() {{
		}};
		
		ImmutableTranslationHint hint = new ImmutableTranslationHint(key, args, fallback);
		
		assertThat(hint.getKey(), is(key));
		assertThat(hint.getArguments(), is(args));
		assertThat(hint.getFallback(), is(fallback));
	}
	
	
	@Test
	public void testKeyNull(
			
		@Mocked List<?> args,
		@Mocked Optional<String> fallback
			
	) throws Exception {
		
		new Expectations() {{
		}};
		
		thrown.expect(NullPointerException.class);
		new ImmutableTranslationHint(null, args, fallback);
	}
	
	
	@Test
	public void testArgsNull(
			
		@Mocked Object key,
		@Mocked Optional<String> fallback
			
	) throws Exception {
		
		new Expectations() {{
		}};
		
		thrown.expect(NullPointerException.class);
		new ImmutableTranslationHint(key, null, fallback);
	}
	
	
	@Test
	public void testFallbackNull(
			
		@Mocked Object key,
		@Mocked List<?> args
			
	) throws Exception {
		
		new Expectations() {{
		}};
		
		thrown.expect(NullPointerException.class);
		new ImmutableTranslationHint(key, args, null);
	}
}
