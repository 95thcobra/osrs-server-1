package nl.bartpelle.veteres.toolkit.dec.impl.stmt;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Stmt;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

/**
 * Created by bart on 7/21/15.
 */
public class AssignVarStmt extends Stmt {

	private boolean string;
	private int idx;
	private Expr expr;

	public AssignVarStmt(Stmt prev, int idx, boolean string, Expr expr) {
		super(prev);

		this.idx = idx;
		this.expr = expr;
		this.string = string;
	}

	@Override public void toCode(CodePrinter printer) {
		if (string) {
			printer.print("strlocal"+idx+" = ");
		} else {
			printer.print("intlocal"+idx+" = ");
		}
		expr.toCode(printer);
		printer.println(";");
	}

}
