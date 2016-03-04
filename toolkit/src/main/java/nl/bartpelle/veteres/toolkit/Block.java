package nl.bartpelle.veteres.toolkit;

import nl.bartpelle.veteres.toolkit.dec.Tree;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by Bart on 17-2-2015.
 *
 * A basic block.
 */
public class Block {

	private static int uid = 0;

	private int id;
	private List<Instruction> instructions = new LinkedList<>();
	private List<Block> successors = new LinkedList<>();
	private List<Block> predecessors = new LinkedList<>();
	private List<Block> dominated = new LinkedList<>();
	private boolean source;
	private boolean sink;
	private Block prev;
	private Block next;
	private Tree tree;
	private FlowGraph graph;

	public Block(FlowGraph graph) {
		id = uid++;
		this.graph = graph;
		dominated.add(this); /* Always dominate self */
	}

	public FlowGraph graph() {
		return graph;
	}

	public int id() {
		return id;
	}

	public List<Instruction> instructions() {
		return instructions;
	}

	public Instruction byAddress(int addr) {
		return instructions.stream().filter(i -> i.addr == addr).findAny().orElse(null);
	}

	public void add(Instruction instruction) {
		instructions.add(instruction);
	}

	public void addSuccessor(Block block) {
		if (!successors.contains(block))
			successors.add(block);
	}

	public void addPredecessor(Block block) {
		if (!predecessors.contains(block))
			predecessors.add(block);
	}

	public void source(boolean b) {
		source = b;
	}

	public boolean isSource() {
		return source;
	}

	public void sink(boolean b) {
		sink = b;
	}

	public boolean isSink() {
		return sink;
	}

	public void dominate(Block block) {
		if (!dominated.contains(block))
			dominated.add(block);
	}

	public boolean dominates(Block block) {
		return dominated.contains(block);
	}

	public List<Block> dominated() {
		return dominated;
	}

	public List<Block> successors() {
		return successors;
	}

	public List<Block> idoms() {
		List<Block> idoms = new LinkedList<>(dominated);
		idoms.retainAll(successors);
		return idoms;
	}

	public List<Block> predecessors() {
		return predecessors;
	}

	public Instruction first() {
		return instructions.get(0);
	}

	public Instruction last() {
		return instructions.get(instructions.size() - 1);
	}

	public void next(Block next) {
		this.next = next;
	}

	public void prev(Block prev) {
		this.prev = prev;
	}

	public Block next() {
		return next;
	}

	public Block prev() {
		return prev;
	}

	public void tree(Tree tree) {
		this.tree = tree;
	}

	public Tree tree() {
		return tree;
	}

	public String toGraphString() {
		String suc = successors.stream().map(b -> b.id + "").collect(Collectors.joining(", ", "[", "]"));
		String pred = predecessors.stream().map(b -> b.id + "").collect(Collectors.joining(", ", "[", "]"));
		String doms = dominated.stream().map(b -> b.id + "").collect(Collectors.joining(", ", "[", "]"));

		StringBuilder builder = new StringBuilder();
		builder.append("block_").append(id).append("[label=\"");
		instructions.forEach(i -> builder.append("#").append(i.addr).append("\t").append(i.opcode).append(",\t").append(i.i).append(",\t").append(i.s).append("\\l"));
		builder.append("\", shape=box];\n");

		for (Block b : successors) {
			builder.append("block_"+id+" -> block_" + b.id + ";\n");
		}

		//StringBuilder sb = new StringBuilder("Block["+id+"] (suc: " + suc + ", pred: " + pred + ", dom: " + doms + ") {\n");
		//instructions.forEach(i -> sb.append("\t#").append(i.addr).append("\t").append(i.opcode).append(", ").append(i.i).append(", ").append(i.s).append("\n"));
		//return sb.append("}").toString();
		return builder.toString();
	}

	public String toString() {
		String suc = successors.stream().map(b -> b.id + "").collect(Collectors.joining(", ", "[", "]"));
		String pred = predecessors.stream().map(b -> b.id + "").collect(Collectors.joining(", ", "[", "]"));
		String doms = dominated.stream().map(b -> b.id + "").collect(Collectors.joining(", ", "[", "]"));
		return "Block_" + id + "[pred="+pred+", suc="+suc+", doms="+doms+"]";
	}

}
