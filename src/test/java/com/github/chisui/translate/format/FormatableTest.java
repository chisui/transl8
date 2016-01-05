package com.github.chisui.translate.format;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import org.junit.Test;

import com.github.chisui.translate.Translator;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

public class FormatableTest {
	
	@Test
	public void testThan(
			
		@Mocked Appendable appendable,
		@Mocked List<?> args,
		@Mocked Translator translator
	
	) throws Exception {
		
		new Expectations() {{
			appendable.append("foo");
			appendable.append("bar");
		}};
		
		Formatable
				.of("foo")
				.than("bar")
				.format(appendable, args, translator);
		
	}
	
	@Test
	public void testThanNoop() throws Exception {
		
		Formatable formatable = Formatable.of("foo");

		assertThat(formatable.than(Formatable.noop()), is(formatable));
	}
	
	@Test
	public void testThanBiFunction(
			
		@Mocked Appendable appendable,
		@Mocked List<?> args,
		@Mocked Translator translator,
		
		@Mocked BiFunction<List<?>, Translator, String> f
	
	) throws Exception {
		
		new Expectations() {{
			appendable.append("foo");
			f.apply(args, translator); returns("bar");
			appendable.append("bar");
		}};
		
		Formatable
				.of("foo")
				.than(f)
				.format(appendable, args, translator);
		
	}

	@Test
	public void testNoop(
	
		@Mocked Appendable appendable,
		@Mocked List<?> args,
		@Mocked Translator translator
			
	) throws Exception {
		
		new Expectations() {{ }};
		
		Formatable.noop()
			.format(appendable, args, translator);
		
		new Verifications() {{ }};
	}
	
	@Test
	public void testNoopThan(
	
		@Mocked Formatable formatable
			
	) throws Exception {
		assertThat(Formatable.noop().than(formatable), is(formatable));
	}
	
	@Test
	public void testNoopToString() throws Exception {
		
		assertThat(Formatable.noop().toString(), is("Formatable []"));
	}
	
	
	@Test
	public void testNoopEquals() throws Exception {
		
		Set<Formatable> noops = new HashSet<>();
		
		noops.add(Formatable.noop());
		noops.add(Formatable.noop());
		
		assertThat(noops, contains(Formatable.noop()));
	}
	
	@Test
	public void testNoopEqualsDifferent() throws Exception {
		
		assertThat(Formatable.noop().equals("foo"), is(false));
	}
	
	@Test
	public void testOfString(
	
		@Mocked Appendable appendable,
		@Mocked List<?> args,
		@Mocked Translator translator
			
	) throws Exception {
		
		new Expectations() {{
			appendable.append("foo");
		}};
		
		Formatable.of("foo")
				.format(appendable, args, translator);
	}	

	@Test
	public void testOfStringToString() throws Exception {
		
		assertThat(Formatable.of("foo").toString(), is("Formatable [append(\"foo\")]"));
		
	}
	
	@Test
	public void testOfStringEquals() throws Exception {

		Set<Formatable> fx = new HashSet<>();
		
		fx.add(Formatable.of("foo"));
		fx.add(Formatable.of("foo"));
		fx.add(Formatable.of("bar"));
		
		assertThat(fx, containsInAnyOrder(
				Formatable.of("foo"),
				Formatable.of("bar")));
	}
	
	@Test
	public void testOfStringEqualsSame() throws Exception {

		Formatable f = Formatable.of("foo");
		assertTrue(f.equals(f));
	}
	
	@Test
	public void testOfStringEqualsDifferent() throws Exception {
		assertThat(Formatable.of("foo"), not(is("foo")));
	}
	
	@Test
	public void testOfBiFunction(
	
		@Mocked Appendable appendable,
		@Mocked List<?> args,
		@Mocked Translator translator,
		
		@Mocked BiFunction<List<?>, Translator, String> f
			
	) throws Exception {
		
		new Expectations() {{
			f.apply(args, translator); returns("foo");
			
			appendable.append("foo");
		}};
		
		Formatable.of(f)
				.format(appendable, args, translator);
	}
}

