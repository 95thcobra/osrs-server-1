package nl.bartpelle.veteres.toolkit.dec.impl.stmt;

import nl.bartpelle.veteres.toolkit.Block;
import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Stmt;

/**
 * Created by Bart on 17-2-2015.
 */
public class CmpStmt extends Stmt {

	private Expr condition;
	private Stmt trueArm;
	private Stmt falseArm;

	public CmpStmt(Stmt prev, Expr condition, Stmt trueArm, Stmt falseArm) {
		super(prev);

		this.condition = condition;
		this.trueArm = trueArm;
		this.falseArm = falseArm;
	}

	public Expr condition() {
		return condition;
	}

	public Stmt trueArm() {
		return trueArm;
	}

	public Stmt falseArm() {
		return falseArm;
	}

}
