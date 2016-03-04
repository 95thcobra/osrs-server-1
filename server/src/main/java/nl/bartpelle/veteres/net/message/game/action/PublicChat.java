package nl.bartpelle.veteres.net.message.game.action;

import io.netty.channel.ChannelHandlerContext;
import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.ChatMessage;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.net.message.game.Action;
import nl.bartpelle.veteres.net.message.game.PacketInfo;
import nl.bartpelle.veteres.util.HuffmanCodec;

/**
 * Created by Bart Pelle on 8/23/2014.
 */
@PacketInfo(size = -1)
public class PublicChat implements Action {

	private int effect;
	private int color;
	private int len;
	private byte[] data;

	@Override public void process(Player player) {
		// Decode huffman data
		byte[] stringData = new byte[256];
		HuffmanCodec codec = player.world().server().huffman();
		codec.decode(data, stringData, 0, 0, len);
		String message = new String(stringData, 0, len);

		ChatMessage chatMessage = new ChatMessage(message, effect, color);
		player.sync().publicChatMessage(chatMessage);
	}

	@Override public void decode(RSBuffer buf, ChannelHandlerContext ctx, int opcode, int size) {
		int unid = buf.readByte();
		color = buf.readByte();
		effect = buf.readByte();

		len = buf.readCompact();
		data = new byte[buf.get().readableBytes()];
		buf.get().readBytes(data);
	}

}
