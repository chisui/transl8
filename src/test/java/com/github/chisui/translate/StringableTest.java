package com.github.chisui.translate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.util.function.Supplier;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;

public class StringableTest {

	@Test
	public void testOfString() throws Exception {

		assertThat(Stringable.of("foo").toString(), is("foo"));
		
	}
	
	@Test
	public void testOfSupplier(
	
		@Mocked Supplier<String> supplier
			
	) throws Exception {
		
		new Expectations() {{
			supplier.get(); returns("foo");
		}};
		
		assertThat(Stringable.of(supplier).toString(), is("foo"));
		
	}

}
