package nl.bartpelle.veteres.toolkit;

import nl.bartpelle.veteres.toolkit.dec.Tree;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Bart on 17-2-2015.
 */
public class FlowGraph {

	private List<Block> blocks;
	private int[] instructions;
	private int[] ints;
	private String[] strings;
	private HashMap[] caseTables;
	private Block root;
	private CS2Script script;

	public FlowGraph(CS2Script script) {
		this.script = script;

		instructions = script.instructions;
		ints = script.integerOperands;
		strings = script.stringOperands;
		caseTables = script.casetables;

		buildBlocks();
	}

	public void buildBlocks() {
		blocks = new LinkedList<>();
		boolean[] headers = new boolean[instructions.length];

		/* Discover headers */
		for (int i=0; i<headers.length; i++) {
			if (isJumpInstruction(instructions[i])) {
				//headers[i] = true;
				headers[i + 1] = true;

				if (isCaseSwitch(instructions[i])) {
					HashMap table = caseTables[ints[i]];

					for (Object targ : table.values()) {
						headers[i + (int) targ + 1] = true;
					}
				} else {
					headers[i + 1 + ints[i]] = true;
				}
			}/* else if (instructions[i] == 21) {
				headers[i] = true;
			}*/
		}

		/* Create blocks */
		Block activeBlock = root = new Block(this);
		activeBlock.source(true);
		blocks.add(activeBlock);
		System.out.println("done");

		for (int i=0; i<instructions.length; i++) {
			if (headers[i]) {
				Block next = new Block(this);
				blocks.add(next);

				activeBlock = next;
			}

			activeBlock.add(new Instruction(i, instructions[i], ints[i], strings[i]));
		}

		System.out.println("doner");

		/* Set successors and predecessors */
		for (int i=0; i<instructions.length; i++) {
			/* Jump targets */
			if (isJumpInstruction(instructions[i])) {
				Block src = forAddress(i);

				if (isCaseSwitch(instructions[i])) {
					HashMap table = caseTables[ints[i]];

					for (Object targ : table.values()) {
						Block target = forAddress(i + (int)targ + 1);
						src.addSuccessor(target);
						target.addPredecessor(src);
					}
				} else {
					Block target = forAddress(i + 1 + ints[i]);
					src.addSuccessor(target);
					target.addPredecessor(src);
				}
			}

			/* Fall-through targets */
			if (!isUnconditionalJump(instructions[i])) {
				Block src = forAddress(i);
				Block target = forAddress(i + 1);

				if (src != target && target != null) {
					src.addSuccessor(target);
					target.addPredecessor(src);
				}
			}
		}



		blocks.get(blocks.size() - 1).sink(true);


		/* Dominator calculation */
		boolean changed = true;
		while (changed) {
			changed = false;

			for (Block checking : blocks) {
				/* Check if this block has only one dominated */
				Block possible = null;
				for (Block potential : blocks) {
					if (checking.predecessors().contains(potential) && !checking.dominates(possible)) {
						if (possible != null) { /* Is this the second one? Poof. No longer possible. */
							possible = null;
							break;
						}

						possible = potential;
					}
				}

				if (possible != null && !possible.dominates(checking)) {
					possible.dominate(checking);
					changed = true;
					break;
				}
			}
		}

		// Set prev/nexts
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);

			if (i != 0)
				block.prev(blocks.get(i - 1));
			if (i != blocks.size() - 1)
				block.next(blocks.get(i + 1));
		}

		// Create trees honey
		for (Block block : blocks) {
			new Tree(this, block).buildStructure();
		}
	}

	public String decompile() {
		CodePrinter printer = new CodePrinter();

		writeSignature(printer);
		printer.println(" {").inc();
		root.tree().toCode(printer);
		printer.dec();
		printer.println("}");

		return printer.toString();
	}

	private void writeSignature(CodePrinter printer) {
		printer.print("public struct script" + script.id + "(");

		for (int i=0; i<script.numLocalInts; i++) {
			printer.print("int intlocal"+i);
			if (i < script.numLocalInts - 1)
				printer.print(", ");
		}

		for (int i=0; i<script.numLocalStrings; i++) {
			printer.print("String strlocal"+i);
			if (i < script.numLocalStrings - 1)
				printer.print(", ");
		}

		printer.print(")");
	}

	public Block blockById(int id) {
		return blocks().stream().filter(b -> b.id() == id).findFirst().orElseGet(null);
	}

	public Map caseTable(int id) {
		return caseTables[id];
	}

	public Block forAddress(int addr) {
		return blocks.stream().filter(b -> b.byAddress(addr) != null).findAny().orElse(null);
	}

	public List<Block> blocks() {
		return blocks;
	}

	public static boolean isJumpInstruction(int i) {
		return (i >= 6 && i <= 10) || i == 31 || i == 32 || i == 51;
	}

	public static boolean isConditionalJump(int i) {
		return (i >= 7 && i <= 10) || i == 31 || i == 32 || i == 51;
	}

	public static boolean isCaseSwitch(int i) {
		return i == 51;
	}

	public static boolean isUnconditionalJump(int i) {
		return i == 6;
	}

}
