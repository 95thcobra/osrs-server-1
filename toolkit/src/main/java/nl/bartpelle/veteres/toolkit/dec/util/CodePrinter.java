package nl.bartpelle.veteres.toolkit.dec.util;

/**
 * Created by bart on 7/21/15.
 */
public class CodePrinter {

	private static final String TABS = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";

	private StringBuilder builder = new StringBuilder();
	private int indent;
	private boolean fresh = false;

	public CodePrinter() {

	}

	private String tabs() {
		return TABS.substring(0, indent);
	}

	public CodePrinter inc() {
		indent++;
		return this;
	}

	public CodePrinter dec() {
		if (indent > 0)
			indent--;
		return this;
	}

	public CodePrinter newline() {
		builder.append("\n").append(tabs());
		return this;
	}

	public CodePrinter print(String s) {
		if (fresh) {
			fresh = false;
			newline();
		}
		builder.append(s);
		return this;
	}

	public CodePrinter println(String s) {
		print(s);
		fresh = true;
		return this;
	}

	@Override public String toString() {
		return builder.toString();
	}
}
