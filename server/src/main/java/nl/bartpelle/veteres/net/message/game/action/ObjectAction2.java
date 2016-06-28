package nl.bartpelle.veteres.net.message.game.action;

import io.netty.channel.ChannelHandlerContext;
import nl.bartpelle.veteres.aquickaccess.actions.ObjectClick2Action;
import nl.bartpelle.veteres.aquickaccess.events.ClickObjectEvent;
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

/**
 * Created by Bart on 8/23/2015.
 */
@PacketInfo(size = 7)
public class ObjectAction2 implements Action {

    private int id;
    private int x;
    private int z;
    private boolean run;

    @Override
    public void decode(RSBuffer buf, ChannelHandlerContext ctx, int opcode, int size) {
        id = buf.readUShort();
        run = buf.readByte() == 1;
        z = buf.readUShortA();
        x = buf.readUShortA();
    }

    @Override
    public void process(Player player) {
        MapObj obj = player.world().objById(id, x, z, player.tile().level);

        if (obj == null)
            return;

        if (player.attrib(AttributeKey.DEBUG, false))
            player.message("Interacting with object %d at [%d, %d]", id, x, z);

        if (!player.locked() && !player.dead()) {
            //player.stopActions(true);
            //player.putattrib(AttributeKey.INTERACTION_OBJECT, obj);
            //player.putattrib(AttributeKey.INTERACTION_OPTION, 2);
            //player.world().server().scriptExecutor().executeScript(player, ObjectInteraction.script);


            player.stopActions(true);
            player.putattrib(AttributeKey.INTERACTION_OBJECT, obj);
            player.putattrib(AttributeKey.INTERACTION_OPTION, 1);

            player.walkTo(obj, PathQueue.StepType.REGULAR);
            player.faceObj(obj);
            Tile targetTile = player.pathQueue().getTargetTile();
            player.world().getEventHandler().addEvent(player, new Event() {
                @Override
                public void execute(EventContainer container) {
                    if (player.tile().distance(targetTile) <= 3) {
                        new ObjectClick2Action().handleObjectClick(player, obj);
                        container.stop();
                    }
                }
            });
        }
    }

}
