package nl.bartpelle.veteres.toolkit.dec;

import nl.bartpelle.veteres.toolkit.Block;
import nl.bartpelle.veteres.toolkit.FlowGraph;
import nl.bartpelle.veteres.toolkit.Instruction;
import nl.bartpelle.veteres.toolkit.dec.impl.expr.*;
import nl.bartpelle.veteres.toolkit.dec.impl.stmt.*;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

import java.util.*;

/**
 * Created by bart on 7/20/15.
 */
public class Tree {

	private FlowGraph graph;
	private Block block;
	private List<Stmt> stmts = new LinkedList<>();

	public Tree(FlowGraph graph, Block block) {
		this.graph = graph;
		this.block = block;

		block.tree(this);
	}

	public FlowGraph graph() {
		return graph;
	}

	public List<Stmt> stmts() {
		return stmts;
	}

	public void toCode(CodePrinter printer) {
		stmts.forEach(s -> s.toCode(printer));
	}

	public void buildStructure() {
		Stack<Expr> exprsInt = new Stack<>();
		Stack<Expr> exprsStr = new Stack<>();

		// See what we can turn this into
		for (Instruction instruction : block.instructions()) {
			System.out.println(instruction.opcode);
			if (FlowGraph.isCaseSwitch(instruction.opcode)) {
				Expr condition = exprsInt.pop();

				Block defaultArm = block.next();
				Map<Integer, Block> arms = new HashMap<>();

				Map caseTable = graph.caseTable(instruction.i);
				caseTable.forEach((k, v) -> {
					Block arm = graph.forAddress((int) v + instruction.addr + 1);
					assert arm != null;
					arms.put((int) k, arm);
				});

				//System.out.println("Case table built. Mapped this way around, default as " + defaultArm.id() + ":");
				//arms.forEach((v, b) -> {
				//	System.out.println("\t" + v + " maps to " + b.id());
				//});

				SwitchStmt switchStmt = new SwitchStmt(null, condition);
				switchStmt.setDefault(new BasicBlockStmt(null, defaultArm));
				arms.forEach((v, b) -> switchStmt.addArm((int) v, new BasicBlockStmt(null, b)));
				stmts.add(switchStmt);
				return;
			} else if (FlowGraph.isConditionalJump(instruction.opcode)) {
				Expr right = exprsInt.pop();
				Expr left = exprsInt.pop();

				Block falseArm = block.next();
				Block trueArm = graph.forAddress(instruction.addr + instruction.i + 1);
				IfStmt stmt = new IfStmt(null, instruction.opcode, left, right, new BasicBlockStmt(null, trueArm), new BasicBlockStmt(null, falseArm));
				stmts.add(stmt);
				return;
			} else if (instruction.opcode == 0) {
				exprsInt.push(new IntExpr(null, instruction.i));
			} else if (instruction.opcode == 1) {
				exprsInt.push(new VarpExpr(null, instruction.i));
			} else if (instruction.opcode == 33) {
				exprsInt.push(new IntVarExpr(null, instruction.i));
			} else if (instruction.opcode == 34) {
				stmts.add(new AssignVarStmt(null, instruction.i, false, exprsInt.pop()));
			} else if (instruction.opcode == 3) {
				exprsStr.push(new StringExpr(null, instruction.s));
			} else if (instruction.opcode == 36) {
				stmts.add(new AssignVarStmt(null, instruction.i, true, exprsStr.pop()));
			} else if (instruction.opcode == 37) {
				exprsStr.push(new StrCatExpr(null, reversePop(exprsStr, instruction.i)));
			} else if (instruction.opcode == 21) {
				stmts.add(new ReturnStmt(null, popAll(exprsInt, exprsStr)));
			} else if (instruction.opcode == 25) {
				exprsInt.push(new VarbitExpr(null, instruction.i));
			} else if (instruction.opcode == 200) {
				exprsInt.push(new FuncExpr(null, "loadinterface", Type.INT, reversePop(exprsInt, 2)));
			} else if (instruction.opcode == 2105) {
				Expr[] args = reversePop(exprsInt, 2);
				stmts.add(new ExprStmt(null, new FuncExpr(null, "setDisabledSprite", Type.VOID, args)));
			} else if (instruction.opcode == 4010) {
				Expr[] args = reversePop(exprsInt, 2);
				exprsInt.add(new FuncExpr(null, "check_bit", Type.INT, args));
			} else if (instruction.opcode == 4003) {
				Expr a = exprsInt.pop();
				Expr b = exprsInt.pop();
				exprsInt.push(new ArithExpr(null, a, b, ArithExpr.Operation.DIVIDE));
			} else if (instruction.opcode == 4011) {
				Expr a = exprsInt.pop();
				Expr b = exprsInt.pop();
				exprsInt.push(new ArithExpr(null, a, b, ArithExpr.Operation.MODULO));
			} else if (instruction.opcode == 4014) {
				Expr a = exprsInt.pop();
				Expr b = exprsInt.pop();
				exprsInt.push(new ArithExpr(null, a, b, ArithExpr.Operation.AND));
			} else if (instruction.opcode == 4106) {
				exprsStr.push(new ToStringExpr(null, exprsInt.pop()));
			} else if (instruction.opcode != 6) {
				exprsInt.push(new Expr(null, Type.VOID));
				System.out.println("Missing op " + instruction);
				throw new RuntimeException("FUKIN YASSIN GA BESSEN PLUKKEN OFZO " + instruction);
			}
		}
	}

	private static Expr[] reversePop(Stack<Expr> stack, int num) {
		Expr[] e = new Expr[num];
		for (int i=num-1; i>=0; i--) {
			e[i] = stack.pop();
		}
		return e;
	}

	private static Expr[] popAll(Stack<Expr> stack1, Stack<Expr> stack2) {
		Expr[] e = new Expr[stack1.size() + stack2.size()];
		if (stack1.size() > 0) {
			int sz = stack2.size();
			for (int i = stack1.size() - 1; i >= 0; i--) {
				e[i + sz] = stack1.pop();
			}
		}
		if (stack2.size() > 0) {
			for (int i = stack2.size() - 1; i >= 0; i--) {
				e[i] = stack2.pop();
			}
		}
		return e;
	}

}
