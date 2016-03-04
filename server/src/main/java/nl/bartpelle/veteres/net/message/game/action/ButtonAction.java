package nl.bartpelle.veteres.net.message.game.action;

import io.netty.channel.ChannelHandlerContext;
import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.AttributeKey;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.model.entity.player.Privilege;
import nl.bartpelle.veteres.net.message.game.Action;
import nl.bartpelle.veteres.net.message.game.PacketInfo;

/**
 * Created by Bart on 5-2-2015.
 */
@PacketInfo(size = 8)
public class ButtonAction implements Action {

	public static final int[] OPCODES = {207, 233, 83, 103, 160, 166, 192, 35, 176, 32};

	private int option;
	private int hash;
	private int item;
	private int slot;

	@Override
	public void decode(RSBuffer buf, ChannelHandlerContext ctx, int opcode, int size) {
		hash = buf.readInt();
		slot = buf.readUShort();
		item = buf.readUShort();

		if (item == 0xFFFF)
			item = -1;
		if (slot == 0xFFFF)
			slot = -1;

		/* Resolve option based on opcode */
		for (int i = 0; i < OPCODES.length; i++)
			if (OPCODES[i] == opcode)
				option = i;
	}

	@Override
	public void process(Player player) {
		player.world().server().scriptRepository().triggerButton(player, hash>>16, hash&0xFFFF, slot, option + 1);
	}
}