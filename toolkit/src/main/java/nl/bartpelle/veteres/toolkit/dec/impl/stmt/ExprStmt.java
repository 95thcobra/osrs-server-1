package nl.bartpelle.veteres.toolkit.dec.impl.stmt;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Stmt;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

/**
 * Created by Bart on 17-2-2015.
 */
public class ExprStmt extends Stmt {

	private Expr expression;

	public ExprStmt(Stmt prev, Expr e) {
		super(prev);
		expression = e;
	}

	public Expr expression() {
		return expression;
	}

	@Override
	public void toCode(CodePrinter printer) {
		expression.toCode(printer);
		printer.println(";");
	}
}
