package nl.bartpelle.veteres.toolkit.dec.impl.expr;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Type;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

/**
 * Created by Bart on 7/28/2015.
 */
public class ToStringExpr extends Expr {

	private Expr expr;

	public ToStringExpr(Expr parent, Expr expr) {
		super(parent, Type.STRING);
		this.expr = expr;
	}

	@Override
	public void toCode(CodePrinter printer) {
		printer.print("String.valueOf(");
		expr.toCode(printer);
		printer.print(")");
	}
}
