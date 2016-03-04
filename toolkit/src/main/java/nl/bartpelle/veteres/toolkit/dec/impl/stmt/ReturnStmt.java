package nl.bartpelle.veteres.toolkit.dec.impl.stmt;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Stmt;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by bart on 7/21/15.
 */
public class ReturnStmt extends Stmt {

	private Expr[] returnvals;

	public ReturnStmt(Stmt prev, Expr... stack) {
		super(prev);

		returnvals = stack;
	}

	@Override public void toCode(CodePrinter printer) {
		if (returnvals.length == 0)
			printer.println("return;");
		else if (returnvals.length == 1) {
			printer.print("return ");
			returnvals[0].toCode(printer);
			printer.println(";");
		}
		else {
			printer.print("return structOf(");

			for (int i=0; i<returnvals.length; i++) {
				returnvals[i].toCode(printer);
				if (i < returnvals.length - 1)
					printer.print(", ");
			}

			printer.println(");");
		}
	}

}
