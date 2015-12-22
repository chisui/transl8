package net.chisui.transl8.format;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;

import net.chisui.transl8.Translator;

@FunctionalInterface
public interface Formatable {
	
	void format(Appendable appendable,
			 List<?> args,
			 Translator translator) throws IOException;
	
	default Formatable than(Formatable nextFormatable) {
		requireNonNull(nextFormatable);
		if (noop().equals(nextFormatable)) {
			return this;
		} else {
			return (a, args, t) -> {
				 this.format(a, args, t);
				 nextFormatable.format(a, args, t);
			 };
		}
	}
	
	default Formatable than(BiFunction<List<?>, Translator, String> f) {
		return than(of(f));
	}
	
	default Formatable than(String str) {
		return than(of(str));
	}

	static Formatable noop() {
		return NoopFormatable.of();
	}
	
	class NoopFormatable implements Formatable {

		private static final NoopFormatable NOOP_FORMATABLE = new NoopFormatable();

		private NoopFormatable() {
		}

		public static NoopFormatable of() {
			return NOOP_FORMATABLE;
		}

		@Override
		public void format(Appendable appendable, List<?> args, Translator translator) throws IOException {
		}

		@Override
		public Formatable than(Formatable nextFormatable) {
			return nextFormatable;
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj == this;
		}
		
		@Override
		public int hashCode() {
			return 1;
		}
		
		@Override
		public String toString() {
			return "Formatable []";
		}
	}
	
	
	static Formatable of(BiFunction<List<?>, Translator, String> f) {
		requireNonNull(f);
		return (a, args, t) -> a.append(f.apply(args, t));
	}
	
	
	static StringFormatable of( String str) {
		return new StringFormatable(str);
	}
	
	class StringFormatable implements Formatable {

		private final String str;

		public StringFormatable(String str) {
			this.str = requireNonNull(str);
		}

		@Override
		public void format(Appendable appendable, List<?> args, Translator translator) throws IOException {
			appendable.append(str);
		}

		@Override
		public String toString() {
			return "Formatable [append(\""+ str + "\")]";
		}

		@Override
		public int hashCode() {
			return str.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			} else if (obj instanceof StringFormatable) {
				StringFormatable that = (StringFormatable) obj;
				return this.str.equals(that.str);
			} else {
				return false;
			}
		}
		
	}
	
}
