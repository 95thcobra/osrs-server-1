package nl.bartpelle.veteres.net.message.game;

import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.entity.Player;

/**
 * Created by Bart Pelle on 8/22/2014.
 */
public class SetPlayerOption implements Command {

	private int slot;
	private boolean top;
	private String option;

	public SetPlayerOption(int slot, boolean top, String option) {
		this.slot = slot;
		this.top = top;
		this.option = option;
	}

	@Override
	public RSBuffer encode(Player player) {
		//player.message("setplayeroption sent: slot:"+slot+" top:"+top+" option:"+option);

		RSBuffer buffer = new RSBuffer(player.channel().alloc().buffer(1 + 1 + 1 + option.length() + 1 + 1));

		buffer.packet(133).writeSize(RSBuffer.SizeType.BYTE);

		buffer.writeByteS(top ? 1 : 0);
		buffer.writeByteN(slot);
		buffer.writeString(option);

		return buffer;
	}

}
