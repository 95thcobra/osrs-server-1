package nl.bartpelle.veteres.toolkit.dec;

import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

/**
 * Created by Bart on 17-2-2015.
 */
public class Stmt {

	private Stmt next;
	private Stmt prev;

	public Stmt(Stmt prev) {
		this.prev = prev;
	}

	public void next(Stmt next) {
		this.next = next;
	}

	public Stmt next() {
		return next;
	}

	public Stmt prev() {
		return prev;
	}

	public void toCode(CodePrinter printer) {
		printer.println("_NOTIMPL_CODE");
	}

}
