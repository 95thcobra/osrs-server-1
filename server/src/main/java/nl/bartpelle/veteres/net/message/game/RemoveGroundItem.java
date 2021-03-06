package nl.bartpelle.veteres.net.message.game;

import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.GroundItem;
import nl.bartpelle.veteres.model.entity.Player;

/**
 * Created by Bart on 8/22/2015.
 */
public class RemoveGroundItem implements Command {

	private GroundItem item;

	public RemoveGroundItem(GroundItem item) {
		this.item = item;
	}

	@Override
	public RSBuffer encode(Player player) {
		RSBuffer packet = new RSBuffer(player.channel().alloc().buffer(6)).packet(244);

		packet.writeByteN(((item.tile().x % 8) << 4) | (item.tile().z % 8));
		packet.writeShortA(item.item().id());

		return packet;
	}

}

