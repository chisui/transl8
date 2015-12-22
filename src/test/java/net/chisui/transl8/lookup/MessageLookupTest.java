package net.chisui.transl8.lookup;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import net.chisui.transl8.Stringable;

public class MessageLookupTest {

	private final Locale locale = Locale.ENGLISH;
	private final Object key = Stringable.of("key");

	@Test
	public void testMessagePresent(
	
		@Mocked Function<Object, String> toString,
		@Mocked MessageSource messageSource,
		@Mocked Function<String, String> globalFallback,
		
		@Mocked String strKey
			
	) throws Exception {

		
		new Expectations() {{
			toString.apply(key); returns(strKey);
			
			messageSource.findMessage(locale, strKey); returns(Optional.of("hello world"));
		}};
		
		MessageLookup lookup = MessageLookup.of(toString, messageSource, globalFallback);
		String msg = lookup.getMessage(locale, key, Optional.of("fallback"));
		
		assertThat(msg, is("hello world"));
	}
	
	@Test
	public void testGetMessageFallback(
		
		@Mocked Function<Object, String> toString,
		@Mocked MessageSource messageSource,
		@Mocked Function<String, String> globalFallback,
			
		@Mocked String strKey
			
	) throws Exception {

		new Expectations() {{
			toString.apply(key); returns(strKey);
			
			messageSource.findMessage(locale, strKey); returns(Optional.empty());
		}};
		
		MessageLookup lookup = MessageLookup.of(toString, messageSource, globalFallback);
		String msg = lookup.getMessage(locale, key, Optional.of("fallback"));
		
		assertThat(msg, is("fallback"));
	}
	
	@Test
	public void testMessageGlobalFallback(
			
		@Mocked Function<Object, String> toString,
		@Mocked MessageSource messageSource,
		@Mocked Function<String, String> globalFallback,
			
		@Mocked String strKey
			
	) throws Exception {

		new Expectations() {{
			toString.apply(key); returns(strKey);
			
			messageSource.findMessage(locale, strKey); returns(Optional.empty());
			
			globalFallback.apply(anyString); returns("global fallback");
		}};
		
		MessageLookup lookup = MessageLookup.of(toString, messageSource, globalFallback);
		String msg = lookup.getMessage(locale, key, Optional.empty());
		
		assertThat(msg, is("global fallback"));
	}

	@Test
	public void testFallback() throws Exception {
		
		assertThat(MessageLookup.fallback().getMessage(locale, key, Optional.of("foo")), is("foo"));
		
	}
	
	@Test
	public void testFallbackFallback() throws Exception {
		
		assertThat(MessageLookup.fallback().getMessage(locale, key, Optional.empty()), is("???key???"));
		
	}

	@Test
	public void testOfMessageSource(
		
		@Mocked MessageSource messageSource
	
	) throws Exception {
		
		new Expectations() {{
			messageSource.findMessage(locale, "key"); returns(Optional.of("foo"));
		}};

		MessageLookup lookup = MessageLookup.of(messageSource);
		
		assertThat(lookup.getMessage(locale, key, Optional.empty()), is("foo"));
		
	}
	
	@Test
	public void testOfMessageSourceFallback(
		
		@Mocked MessageSource messageSource
	
	) throws Exception {
		
		new Expectations() {{
			messageSource.findMessage(locale, "key"); returns(Optional.empty());
		}};

		MessageLookup lookup = MessageLookup.of(messageSource);
		
		assertThat(lookup.getMessage(locale, key, Optional.empty()), is("???key???"));
		
	}

	@Test
	public void testOfToStringMessageSource(
		
		@Mocked Function<Object, String> toString,
		@Mocked MessageSource messageSource
	
	) throws Exception {
		
		new Expectations() {{
			toString.apply(key); returns("theKey");
			
			messageSource.findMessage(locale, "theKey"); returns(Optional.of("foo"));
		}};

		MessageLookup lookup = MessageLookup.of(toString, messageSource);
		
		assertThat(lookup.getMessage(locale, key, Optional.empty()), is("foo"));
		
	}
	
	@Test
	public void testOfToStringMessageSourceFallback(
		
		@Mocked Function<Object, String> toString,
		@Mocked MessageSource messageSource
	
	) throws Exception {
		
		new Expectations() {{
			toString.apply(key); returns("theKey");
			
			messageSource.findMessage(locale, "theKey"); returns(Optional.empty());
		}};

		MessageLookup lookup = MessageLookup.of(toString, messageSource);
		
		assertThat(lookup.getMessage(locale, key, Optional.empty()), is("???theKey???"));
		
	}
	
	@Test
	public void testOfMessageSourceGlobalFallback(
		
		@Mocked MessageSource messageSource,
		@Mocked Function<String, String> globalFallback
	
	) throws Exception {
		
		new Expectations() {{
			messageSource.findMessage(locale, "key"); returns(Optional.empty());
			
			globalFallback.apply("key"); returns("fallback");
		}};

		MessageLookup lookup = MessageLookup.of(messageSource, globalFallback);
		
		assertThat(lookup.getMessage(locale, key, Optional.empty()), is("fallback"));
		
	}
	
	@Test
	public void testOfToStringMessageSourceGlobalFallback(
		
		@Mocked Function<Object, String> toString,
		@Mocked MessageSource messageSource,
		@Mocked Function<String, String> globalFallback
	
	) throws Exception {
		
		new Expectations() {{
			toString.apply(key); returns("theKey");
			
			messageSource.findMessage(locale, "theKey"); returns(Optional.empty());
			
			globalFallback.apply("theKey"); returns("fallback");
		}};

		MessageLookup lookup = MessageLookup.of(toString, messageSource, globalFallback);
		
		assertThat(lookup.getMessage(locale, key, Optional.empty()), is("fallback"));
	}

}
