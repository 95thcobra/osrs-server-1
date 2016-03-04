package nl.bartpelle.veteres.toolkit.dec.impl.stmt;

import nl.bartpelle.veteres.toolkit.dec.Stmt;

import java.util.List;

/**
 * Created by bart on 7/20/15.
 */
public class RootStmt extends Stmt {

	private List<Stmt> stmts;

	public RootStmt(Stmt prev, List<Stmt> stmts) {
		super(prev);

		this.stmts = stmts;
	}

	public List<Stmt> stmts() {
		return stmts;
	}

}
