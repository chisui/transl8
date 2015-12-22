package net.chisui.transl8;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

public class Stringable {

	private final Supplier<String> toString;
	
	public static Stringable of(String string) {
		requireNonNull(string);
		return of(() -> string);
	}

	public static Stringable of(Supplier<String> toString) {
		return new Stringable(toString);
	}
	
	public Stringable(Supplier<String> toString) {
		this.toString = requireNonNull(toString);
	}

	@Override
	public String toString() {
		return toString.get();
	}

	
}
