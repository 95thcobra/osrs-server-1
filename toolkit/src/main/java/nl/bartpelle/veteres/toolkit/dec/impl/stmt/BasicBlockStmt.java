package nl.bartpelle.veteres.toolkit.dec.impl.stmt;

import nl.bartpelle.veteres.toolkit.Block;
import nl.bartpelle.veteres.toolkit.Instruction;
import nl.bartpelle.veteres.toolkit.dec.Stmt;
import nl.bartpelle.veteres.toolkit.dec.util.CodePrinter;

import java.util.List;

/**
 * Created by bart on 7/20/15.
 */
public class BasicBlockStmt extends Stmt {

	private Block block;

	public BasicBlockStmt(Stmt prev, Block block) {
		super(prev);
		this.block = block;

		// Optimize this block, basically turning a jumpblock into the target block
		if (block.instructions().size() == 1 && block.instructions().get(0).opcode == 6) {
			Instruction instr = block.instructions().get(0);
			this.block = block.graph().forAddress(instr.addr + instr.i + 1);
		}
	}

	public Block block() {
		return block;
	}

	@Override public void toCode(CodePrinter printer) {
		block.tree().toCode(printer);
	}

}
