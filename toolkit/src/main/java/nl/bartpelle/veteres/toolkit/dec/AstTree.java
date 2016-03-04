package nl.bartpelle.veteres.toolkit.dec;

import nl.bartpelle.veteres.toolkit.Block;
import nl.bartpelle.veteres.toolkit.FlowGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Bart on 2/17/2015.
 */
public class AstTree {

	private List<Stmt> statements = new LinkedList<>();
	private FlowGraph graph;

	public AstTree(FlowGraph graph) {
		this.graph = graph;

		decomposeBasicBlocks();
	}

	private void decomposeBasicBlocks() {
		Queue<Block> blocks = new LinkedList<>();
		blocks.addAll(graph.blocks());

		while (!blocks.isEmpty()) {
			Block block = blocks.poll();
		}
	}

	private static void decompose(Block block) {
		// Check for a case switch
		if (FlowGraph.isCaseSwitch(block.last().opcode)) {

		}
	}

}
