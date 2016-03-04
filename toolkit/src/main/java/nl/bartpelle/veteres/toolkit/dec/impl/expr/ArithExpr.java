package nl.bartpelle.veteres.toolkit.dec.impl.expr;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Type;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

/**
 * Created by Bart on 17-2-2015.
 */
public class ArithExpr extends Expr {

	private Expr left;
	private Expr right;
	private Operation operation;

	public ArithExpr(Expr parent, Expr left, Expr right, Operation operation) {
		super(parent, Type.INT);

		this.left = left;
		this.right = right;
		this.operation = operation;
	}

	public Expr left() {
		return left;
	}

	public Expr right() {
		return right;
	}

	public Operation operation() {
		return operation;
	}

	@Override
	public void toCode(CodePrinter printer) {
		right.toCode(printer);
		printer.print(" ");
		printer.print(operation.c + "");
		printer.print(" ");
		left.toCode(printer);
	}

	public static enum Operation {
		ADD('+'), SUBTRACT('-'), MULTIPLY('*'), DIVIDE('/'), MODULO('%'), AND('%');
		char c;
		Operation(char c) {this.c = c;}
	}

}
