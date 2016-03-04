package nl.bartpelle.veteres.toolkit.dec.impl.expr;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Type;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bart on 7/20/15.
 */
public class StrCatExpr extends Expr {

	private Expr[] strings;

	public StrCatExpr(Expr parent, Expr... strings) {
		super(parent, Type.STRING);

		this.strings = strings;
		trim();
	}

	private void trim() {
		List<Expr> exprs = new LinkedList<>();

		Expr active = null;
		for (Expr str : strings) {
			if (active == null) {
				active = str;
				exprs.add(str);
			} else {
				if (active instanceof StringExpr && str instanceof StringExpr) {
					StringExpr stored = ((StringExpr) active);
					stored.data(stored.string() + ((StringExpr) str).string());
				} else {
					active = str;
					exprs.add(str);
				}
			}
		}

		strings = exprs.toArray(new Expr[exprs.size()]);
	}

	@Override public void toCode(CodePrinter printer) {
		for (int i=0; i<strings.length; i++) {
			strings[i].toCode(printer);
			if (i < strings.length - 1)
				printer.print(" + ");
		}
	}

}
