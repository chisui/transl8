package net.chisui.transl8;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
	public void testEqualsTrue() throws Exception {

		assertThat(new AbstractTranslationHintImpl().equals(TranslationHint.of("key", "arg0", "arg1").withFallback("fallback")), is(true));
	}
	
	@Test
	public void testEqualsDifferentKey() throws Exception {

		assertThat(new AbstractTranslationHintImpl().equals(TranslationHint.of("otherKey", "arg0", "arg1").withFallback("fallback")), is(false));
	}
	
	@Test
	public void testEqualsDifferentArgs() throws Exception {

		assertThat(new AbstractTranslationHintImpl().equals(TranslationHint.of("key", "arg0", "arg1", "arg2").withFallback("fallback")), is(false));
	}
	
	@Test
	public void testEqualsNoArgs() throws Exception {

		assertThat(new AbstractTranslationHintImpl().equals(TranslationHint.of("key").withFallback("fallback")), is(false));
	}
	
	@Test
	public void testEqualsDifferentFallback() throws Exception {

		assertThat(new AbstractTranslationHintImpl().equals(TranslationHint.of("key", "arg0", "arg1").withFallback("anotherFallback")), is(false));
	}
	
	@Test
	public void testEqualsNoFallback() throws Exception {

		assertThat(new AbstractTranslationHintImpl().equals(TranslationHint.of("key", "arg0", "arg1")), is(false));
	}
	
	@Test
	public void testHashcode() throws Exception {
		
		assertThat(new AbstractTranslationHintImpl().hashCode() == new AbstractTranslationHintImpl().hashCode(), is(true));
	}

	@Test
	public void testToString() throws Exception {
		
		String str = new AbstractTranslationHintImpl().toString();
		
		
		assertThat(str, is("AbstractTranslationHintImpl [key=\"key\", args=[arg0, arg1], fallback=\"fallback\"]"));
	}


}
