package nl.bartpelle.veteres;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import nl.bartpelle.veteres.util.Tuple;
import nl.bartpelle.veteres.util.UsernameUtilities;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Bart on 7/30/2015.
 */
public class Flooder {

	public static void main(String[] args) throws Exception {
		for (int i=0; i<5; i++) {
			Socket socket = new Socket("server.os-scape.com", 43594);
			Tuple<Integer, byte[]> msg = prepareMessage(i);
			socket.getOutputStream().write(msg.second(), 0, msg.first());
			socket.getOutputStream().flush();
			Thread.sleep(2);

			System.out.println("Total clients: " + i);
		}

		Thread.sleep(100000000);
	}

	private static Tuple<Integer, byte[]> prepareMessage(int ui) {
		ByteBuf buffer = Unpooled.buffer();

		buffer.writeByte(16); //login
		buffer.writeShort(22 + 8 + 2 + 8); // Size

		buffer.writeInt(86); // rev
		buffer.writeByte(0);
		buffer.writeByte(0);

		for (int i=0;i<4;i++)
			buffer.writeInt(4);

		buffer.writeLong(0);

		buffer.writeBytes("bradyb".getBytes()).writeByte(0);
		buffer.writeBytes("kfctastesgr8".getBytes()).writeByte(0);

		return new Tuple<>(buffer.writerIndex(), buffer.array());
	}

}
