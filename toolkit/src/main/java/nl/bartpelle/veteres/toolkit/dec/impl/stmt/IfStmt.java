package nl.bartpelle.veteres.toolkit.dec.impl.stmt;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Stmt;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bart on 7/20/15.
 */
public class IfStmt extends Stmt {

	private Expr left;
	private Expr right;
	private Stmt trueArm;
	private Stmt falseArm;
	private int cmp;

	public IfStmt(Stmt prev, int cmp, Expr left, Expr right, Stmt trueArm, Stmt falseArm) {
		super(prev);
		this.cmp = cmp;
		this.left = left;
		this.right = right;
		this.trueArm = trueArm;
		this.falseArm = falseArm;
	}

	@Override public void toCode(CodePrinter printer) {
		printer.print("if (");
		left.toCode(printer);
		printer.print(" ");

		switch (cmp) {
			case 8:
				printer.print("==");
				break;
			case 9:
				printer.print("<");
				break;
			case 10:
				printer.print(">");
				break;
			case 32:
				printer.print(">=");
				break;
			default:
				throw new RuntimeException("UNKNOWN OPERATOR " + cmp);
		}

		printer.print(" "); // TODO
		right.toCode(printer);
		printer.println(") {");
		printer.inc();

		trueArm.toCode(printer);

		printer.dec();

		if (falseArm == null) {
			printer.println("}");
		} else {
			// Quickhax: make this neat =D
			if (isPrettyFalseArm()) {
				printer.print("} else ");
				falseArm.toCode(printer);
			} else {
				printer.println("} else {").inc();
				falseArm.toCode(printer);
				printer.dec().println("}");
			}
		}
	}

	private boolean isPrettyFalseArm() {
		if (falseArm instanceof BasicBlockStmt) {
			BasicBlockStmt blockstmt = ((BasicBlockStmt) falseArm);
			if (blockstmt.block().tree().stmts().size() == 1) {
				return blockstmt.block().tree().stmts().get(0) instanceof IfStmt;
			}
		}

		return false;
	}

}
