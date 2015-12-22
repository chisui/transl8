package net.chisui.transl8;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import net.chisui.transl8.TranslationHint.AbstractTranslationHint;

public class AbstractTranslationHintTest {
	
	class AbstractTranslationHintImpl extends AbstractTranslationHint {

		@Override public String getKey() { return "key"; }
		@Override public List<?> getArguments() { return Arrays.asList("arg0", "arg1"); }
		@Override public Optional<String> getFallback() { return Optional.of("fallback"); }
		
	}
	
	@Test
	public void testEqualsSame() throws Exception {

		TranslationHint hint = new AbstractTranslationHintImpl();
		
		
		assertThat(hint.equals(hint), is(true));
	}
	
	@Test
	public void testEquals() throws Exception {
		
		Set<Object> hints = new HashSet<>();
		
		hints.add("hello");
		hints.add(TranslationHint.of("key"));
		hints.add(TranslationHint.of("key"));
		hints.add(TranslationHint.of("key").withArguments("arg"));
		hints.add(TranslationHint.of("key").withArguments("arg"));
		hints.add(TranslationHint.of("key").withArguments("arg1"));
		hints.add(TranslationHint.of("key1").withArguments("arg"));
		hints.add(TranslationHint.of("key").withFallback("fallback"));
		hints.add(TranslationHint.of("key").withFallback("fallback"));
		hints.add(TranslationHint.of("key").withFallback("fallback1"));
		hints.add(TranslationHint.of("key").withArguments("arg").withFallback("fallback"));
		hints.add(TranslationHint.of("key").withArguments("arg").withFallback("fallback"));
		hints.add(TranslationHint.of("key1").withArguments("arg").withFallback("fallback"));
		hints.add(TranslationHint.of("key").withArguments("arg1").withFallback("fallback"));
		hints.add(TranslationHint.of("key").withArguments("arg").withFallback("fallback1"));
		
		assertThat(hints, containsInAnyOrder(
				"hello",
				TranslationHint.of("key"),
				TranslationHint.of("key").withArguments("arg"),
				TranslationHint.of("key").withArguments("arg1"),
				TranslationHint.of("key1").withArguments("arg"),
				TranslationHint.of("key").withFallback("fallback"),
				TranslationHint.of("key").withFallback("fallback1"),
				TranslationHint.of("key").withArguments("arg").withFallback("fallback"),
				TranslationHint.of("key1").withArguments("arg").withFallback("fallback"),
				TranslationHint.of("key").withArguments("arg1").withFallback("fallback"),
				TranslationHint.of("key").withArguments("arg").withFallback("fallback1")
		));
	}

	@Test
	public void testToString() throws Exception {
		
		String str = new AbstractTranslationHintImpl().toString();
		
		
		assertThat(str, is("TranslationHint [key=\"key\", args=[arg0, arg1], fallback=\"fallback\"]"));
	}

	@Test
	public void testToStringNoArgs() throws Exception {
		
		String str = TranslationHint
				.of("key")
				.withFallback("fallback")
				.toString();
		
		
		assertThat(str, is("TranslationHint [key=\"key\", fallback=\"fallback\"]"));
	}
	
	@Test
	public void testToStringNoFallback() throws Exception {
		
		String str = TranslationHint
				.of("key")
				.withArguments("arg0", "arg1")
				.toString();
		
		
		assertThat(str, is("TranslationHint [key=\"key\", args=[arg0, arg1]]"));
	}

}
