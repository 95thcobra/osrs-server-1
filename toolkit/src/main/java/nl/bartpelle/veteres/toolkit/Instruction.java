package nl.bartpelle.veteres.toolkit;

/**
 * Created by Bart on 17-2-2015.
 */
public class Instruction {

	public int addr;
	public int opcode;
	public int i;
	public String s;

	public Instruction(int addr, int opcode, int i, String s) {
		this.addr = addr;
		this.opcode = opcode;
		this.i = i;
		this.s = s;
	}

	@Override public String toString() {
		return "Instruction[addr="+addr+", op="+opcode+", i="+i+", str="+s+"]";
	}
}
