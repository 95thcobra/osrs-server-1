package nl.bartpelle.veteres.toolkit.dec.impl.expr;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Type;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

/**
 * Created by bart on 7/20/15.
 */
public class StringExpr extends Expr {

	private String string;

	public StringExpr(Expr parent, String string) {
		super(parent, Type.STRING);

		this.string = string;
	}

	public String string() {
		return string;
	}

	public void data(String data) {
		string = data;
	}

	@Override public void toCode(CodePrinter printer) {
		printer.print("\"" + string + "\"");
	}
}
