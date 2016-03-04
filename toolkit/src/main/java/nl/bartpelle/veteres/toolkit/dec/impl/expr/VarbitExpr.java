package nl.bartpelle.veteres.toolkit.dec.impl.expr;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Type;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

/**
 * Created by bart on 7/20/15.
 */
public class VarbitExpr extends Expr {

	private int id;

	public VarbitExpr(Expr parent, int id) {
		super(parent, Type.INT);

		this.id = id;
	}

	@Override public void toCode(CodePrinter printer) {
		printer.print("varbit_" + id);
	}
}
