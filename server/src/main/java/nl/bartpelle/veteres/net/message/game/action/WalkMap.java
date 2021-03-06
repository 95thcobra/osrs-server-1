package nl.bartpelle.veteres.net.message.game.action;

import io.netty.channel.ChannelHandlerContext;
import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.entity.PathQueue;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.model.entity.player.Privilege;
import nl.bartpelle.veteres.net.message.game.Action;
import nl.bartpelle.veteres.net.message.game.PacketInfo;
import nl.bartpelle.veteres.script.TimerKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Bart Pelle on 8/23/2014.
 */
@PacketInfo(size = -1)
public class WalkMap implements Action {

	private static final Logger logger = LogManager.getLogger();

	private int x;
	private int z;
	private int mode;

	@Override public void decode(RSBuffer buf, ChannelHandlerContext ctx, int opcode, int size) {
		mode = buf.readByteN();
		z = buf.readUShortA();
		x = buf.readULEShortA();
	}

	@Override public void process(Player player) {
		// Mode 2 is ctrl-shift clicking, teleporting to the tile.
		if (mode == 2 && player.privilege().eligibleTo(Privilege.ADMIN)) {
			player.teleport(x, z, player.tile().level);
			return;
		}

		logger.info("Walking to [{}, {}], running: {}.", x, z, mode);
		if (!player.locked() && !player.dead()) {
			player.stopActions(true);
			player.walkTo(x, z, mode == 1 ? PathQueue.StepType.FORCED_RUN : PathQueue.StepType.REGULAR);
		}
	}
}
