package nl.bartpelle.veteres.net.message.game.action;

import io.netty.channel.ChannelHandlerContext;
import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.model.entity.player.Interfaces;
import nl.bartpelle.veteres.net.message.game.Action;
import nl.bartpelle.veteres.net.message.game.PacketInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Bart on 9-7-2015.
 */
@PacketInfo(size = 5)
public class ChangeDisplayMode implements Action {

	private static final Logger logger = LogManager.getLogger(ChangeDisplayMode.class);

	private int displayMode;
	private int displayWidth;
	private int displayHeight;

	@Override
	public void decode(RSBuffer buf, ChannelHandlerContext ctx, int opcode, int size) {
		displayMode = buf.readByte();
		displayWidth = buf.readUShort();
		displayHeight = buf.readUShort();
	}

	@Override
	public void process(Player player) {
		boolean wasResizable = player.interfaces().resizable();
		player.interfaces().resizable(displayMode == 2);

		// If we changed, push it. TODO fix this?
		/*if (wasResizable != player.interfaces().resizable()) {
			player.interfaces().closeAll();
		}*/

		if (player.interfaces().resizable()) {
			player.interfaces().sendResizable();
		} else {
			player.interfaces().sendFixed();
		}

		logger.info("Displaymode change: mode={}, width={}, height={}", displayMode, displayWidth, displayHeight);
	}
}
