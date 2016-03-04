package nl.bartpelle.veteres.net.message.game.action;

import io.netty.channel.ChannelHandlerContext;
import nl.bartpelle.veteres.content.mechanics.ObjectInteraction;
import nl.bartpelle.veteres.event.Event;
import nl.bartpelle.veteres.event.EventContainer;
import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.AttributeKey;
import nl.bartpelle.veteres.model.Tile;
import nl.bartpelle.veteres.model.entity.PathQueue;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.model.map.MapObj;
import nl.bartpelle.veteres.net.message.game.Action;
import nl.bartpelle.veteres.net.message.game.PacketInfo;
import nl.bartpelle.veteres.plugin.impl.LoginPlugin;
import nl.bartpelle.veteres.plugin.impl.ObjectFirstClickPlugin;

/**
 * Created by Bart on 8/23/2015.
 */
@PacketInfo(size = 7)
public class ObjectAction1 implements Action {

    private int id;
    private int x;
    private int z;
    private boolean run;

    @Override
    public void decode(RSBuffer buf, ChannelHandlerContext ctx, int opcode, int size) {
        id = buf.readUShortA();
        run = buf.readByteS() == 1;
        z = buf.readUShort();
        x = buf.readUShortA();
    }

    @Override
    public void process(Player player) {
        MapObj obj = player.world().objById(id, x, z, player.tile().level);

        if (obj == null)
            return;

        if (player.attrib(AttributeKey.DEBUG, false)) {
            player.message("Interacting with object %d at [%d, %d]", id, x, z);
        }

        if (!player.locked() && !player.dead()) {
            player.stopActions(true);
            player.putattrib(AttributeKey.INTERACTION_OBJECT, obj);
            player.putattrib(AttributeKey.INTERACTION_OPTION, 1);

            // player.world().server().scriptExecutor().executeScript(player, ObjectInteraction.script);

            // Execute groovy plugin
            // player.world().getPluginHandler().execute(player, ObjectFirstClickPlugin.class, new ObjectFirstClickPlugin(id, new Tile(x, z)));
            handleObjectAction1(player, obj);
        }
    }

    private void handleObjectAction1(Player player, MapObj obj) {
        switch (id) {
            case 6817: // Prayer altar
                player.faceObj(obj);
                player.walkTo(obj, PathQueue.StepType.REGULAR);

                PathQueue.Step step = player.pathQueue().peekLast();

                Tile lastTile;
                if (step == null)
                    lastTile = player.tile();
                else
                    lastTile = player.pathQueue().peekLast().toTile();

                player.world().getEventHandler().addEvent(player, 1, new Event() {
                    @Override
                    public void execute(EventContainer container) {
                        if (player.tile().equals(lastTile)) {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        player.skills().restorePrayer();
                        player.animate(645);
                    }
                });
                break;
        }

    }

}
