package nl.bartpelle.veteres.toolkit;

import io.netty.buffer.Unpooled;
import nl.bartpelle.veteres.io.RSBuffer;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bart on 7/28/2015.
 */
public class CS2Script {

	public int id;
	public int numInstructions;
	public int numIntParams;
	public int numStringParams;
	public int numLocalInts;
	public int numLocalStrings;
	public HashMap[] casetables;
	public int[] instructions;
	public int[] integerOperands;
	public String[] stringOperands;

	public static CS2Script decode(int id, byte[] script) {
		CS2Script s = new CS2Script();
		s.id = id;

		RSBuffer buffer = new RSBuffer(Unpooled.wrappedBuffer(script));

		int headerend = script.length - 12;
		buffer.get().readerIndex(headerend);

		s.numInstructions = buffer.readInt();
		s.numIntParams = buffer.readUShort();
		s.numStringParams = buffer.readUShort();
		s.numLocalInts = buffer.readUShort();
		s.numLocalStrings = buffer.readUShort();

		/*int numcasetables = buffer.readUByte();
		s.casetables = new HashMap[numcasetables];
		for (int ct = 0; ct < numcasetables; ct++) {
			s.casetables[ct] = new HashMap();

			int sz = buffer.readUShort();
			for (int c = 0; c < sz; c++) {
				int key = buffer.readInt();
				int jump = buffer.readInt();
				s.casetables[ct].put(key, jump);
			}
		}*/

		buffer.get().readerIndex(0);
		String name = buffer.readString();
		s.instructions = new int[s.numInstructions];
		s.integerOperands = new int[s.numInstructions];
		s.stringOperands = new String[s.numInstructions];

		for (int index = 0, op; buffer.get().readerIndex() < headerend; s.instructions[index++] = op) {
			op = buffer.readUShort();
			s.instructions[index] = op;

			if (op == 3) {
				s.stringOperands[index] = buffer.readString();
			} else if (op < 100 && 21 != op && 38 != op && 39 != op) {
				s.integerOperands[index] = buffer.readInt();
			} else {
				s.integerOperands[index] = buffer.readUByte();
			}
		}

		return s;
	}

	public void dasm(PrintStream stream) {
		for (int i = 0; i < numInstructions; i++) {
			stream.println("#"+i + ": " + mnemonics.getOrDefault(instructions[i], instructions[i] + "") + " " + integerOperands[i] + " \"" + stringOperands[i] + "\"");
		}
	}

	private static Map<Integer, String> mnemonics = new HashMap<Integer, String>() {
		{
			put(0, "push_int");
			put(1, "push_config");
			put(2, "pop_config");
			put(3, "push_string");
			put(6, "jump_relative");
			put(7, "jump_ne");
			put(8, "jump_eq");
			put(9, "jump_lt");
			put(10, "jump_gt");
			put(21, "return");
			put(25, "push_varbit");
			put(27, "set_varbit");
			put(31, "jump_lte");
			put(32, "jump_gte");
			put(33, "push_intvar");
			put(34, "pop_intvar");
			put(35, "push_stringvar");
			put(36, "pop_stringvar");
			put(37, "strcat");
			put(38, "popint");
			put(39, "popstring");
			put(40, "run_script");
			put(42, "push_script_int");
			put(43, "pop_script_int");
			put(47, "push_script_str");
			put(48, "pop_script_str");
			put(2112, "set_interface_string");
			put(2419, "set_keypress_script");
			put(2423, "set_open_script");
			put(3301, "get_container_item");
			put(3302, "get_container_amount");
			put(3313, "get_alt_container_item");
			put(3314, "get_alt_container_amount");
			put(3400, "get_enum_str");
			put(3408, "get_enum_val");
			put(4014, "bit_and");
			put(4010, "check_bit");
		}
	};

}
