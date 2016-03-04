package nl.bartpelle.veteres.toolkit.dec.impl.stmt;

import nl.bartpelle.veteres.toolkit.dec.Expr;
import nl.bartpelle.veteres.toolkit.dec.Stmt;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bart on 7/20/15.
 */
public class SwitchStmt extends Stmt {

	private Expr condition;
	private Map<Integer, Stmt> cases = new HashMap<>();
	private Stmt defaultArm;

	public SwitchStmt(Stmt prev, Expr condition) {
		super(prev);
		this.condition = condition;
	}

	public void addArm(int val, Stmt stmt) {
		cases.put(val, stmt);
	}

	public void setDefault(Stmt def) {
		defaultArm = def;
	}

	public Map<Integer, Stmt> cases() {
		return cases;
	}

	@Override public void toCode(CodePrinter printer) {
		printer.print("switch (");
		condition.toCode(printer);
		printer.println(") {");
		printer.inc();

		cases.forEach((k, v) -> {
			printer.print("case ");
			printer.print(k.toString());
			printer.println(":");
			printer.inc();

			v.toCode(printer);
			printer.dec();
		});

		printer.println("default:");
		defaultArm.toCode(printer);
		System.out.println(defaultArm + ", " + ((BasicBlockStmt)defaultArm).block().id());

		printer.dec();
		printer.println("}");
	}
}
