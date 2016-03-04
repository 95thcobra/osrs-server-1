package nl.bartpelle.veteres.toolkit;

import io.netty.buffer.Unpooled;
import nl.bartpelle.veteres.io.RSBuffer;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bart on 2/14/2015.
 */
public class CS2Dasm {

	public static void main(String[] args) throws Exception {
		//DataStore ds = new DataStore("data/filestore", true);

		/*System.out.println(mnemonics);
		for (int i=0; i<ds.getIndex(12).getLastArchiveId(); i++) {
			try {
				FileWriter fw = new FileWriter("cs2dump/" + i + ".cs2");
				dasm(Compression.decompressArchive(ds.getIndex(12).getArchive(i)), fw);
			} catch (Exception e) {}
		}*/

		//for (int i=0; i<ds.getIndex(12).getLastArchiveId(); i++) {
		//	try {
		//		CS2Script scr = CS2Script.decode(i, Compression.decompressArchive(ds.getIndex(12).getArchive(i)));
		//		PrintStream writer = new PrintStream(new File("scriptdasm/" + i + ".txt"));
		//		scr.dasm(writer);
		//		//FlowGraph graph = new FlowGraph(scr);
		//		//Files.write(new File("scriptdump/" + i + ".java").toPath(), graph.decompile().getBytes());
		//	} catch (Exception e) {
		//		System.err.println("Can't do anything with " + i);
		//	}
		//}

		//CS2Script test = CS2Script.decode(252, Compression.decompressArchive(ds.getIndex(12).getArchive(252)));
		//test.dasm();
		//decompile(test);
	}

	private static void decompile(CS2Script script) throws Exception {
		FlowGraph graph = new FlowGraph(script);
		Files.write(new File("scriptdump/" +script.id+ ".java").toPath(), graph.decompile().getBytes());
		graph.blocks().forEach((block) -> {
			System.out.println(block.toGraphString());
			;
		});
	}

}
