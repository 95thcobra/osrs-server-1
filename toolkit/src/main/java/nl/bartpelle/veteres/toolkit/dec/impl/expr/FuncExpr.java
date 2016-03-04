package nl.bartpelle.veteres.toolkit.dec.impl.expr;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Type;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

/**
 * Created by Bart on 2/17/2015.
 */
public class FuncExpr extends Expr {

	private Expr[] args;
	private String name;

	public FuncExpr(Expr parent, String name, Type type, Expr... args) {
		super(parent, type);

		this.name = name;
		this.args = args;
	}

	private Expr[] args() {
		return args;
	}

	private Expr arg(int index) {
		if (index < 0 || index >= args.length)
			return null;
		return args[index];
	}

	@Override
	public void toCode(CodePrinter printer) {
		printer.print(name).print("(");
		for (int i=0; i<args.length; i++) {
			args[i].toCode(printer);
			if (i != args.length - 1)
				printer.print(", ");
		}
		printer.print(")");
	}
}
