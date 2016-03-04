package nl.bartpelle.veteres.toolkit.dec;

import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bart on 17-2-2015.
 */
public class Expr {

	private Expr parent;
	private Type type;
	private List<Expr> children = new LinkedList<>();

	public Expr(Expr parent, Type type) {
		this.parent = parent;
		this.type = type;
	}

	public Expr parent() {
		return parent;
	}

	public void addChild(Expr c) {
		children.add(c);
	}

	public List<Expr> children() {
		return children;
	}

	public Type type() {
		return type;
	}

	public void toCode(CodePrinter printer) {
		printer.print("_EXPR_NOT_IMPL_"+getClass().getSimpleName());
	}

}
