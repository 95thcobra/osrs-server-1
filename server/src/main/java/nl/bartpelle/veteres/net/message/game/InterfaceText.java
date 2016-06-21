package nl.bartpelle.veteres.net.message.game;

import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.util.SettingsBuilder;

/**
 * Created by Bart on 8/11/2015.
 */
public class InterfaceText implements Command {

	private int hash;
	private String text;

	public InterfaceText(int target, int targetChild, String text) {
		hash = (target << 16) | targetChild;
		this.text = text;
	}

	@Override
	public RSBuffer encode(Player player) {
		player.message("interfacetext sent: hash:"+hash+" text:"+text);
		RSBuffer buffer = new RSBuffer(player.channel().alloc().buffer(text.length() + 1 + 1 + 4));
		buffer.packet(20).writeSize(RSBuffer.SizeType.SHORT);

		buffer.writeIntV2(hash);
		buffer.writeString(text);
		return buffer;
	}
}