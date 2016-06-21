package nl.bartpelle.veteres.net.message.game.action;

import io.netty.channel.ChannelHandlerContext;
import nl.bartpelle.veteres.aquickaccess.ObjectClick1Action;
import nl.bartpelle.veteres.aquickaccess.events.ClickObjectEvent;
import nl.bartpelle.veteres.content.mechanics.ObjectInteraction;
import nl.bartpelle.veteres.event.Event;
import nl.bartpelle.veteres.event.EventContainer;
import nl.bartpelle.veteres.io.RSBuffer;
import nl.bartpelle.veteres.model.AttributeKey;
import nl.bartpelle.veteres.model.ForceMovement;
import nl.bartpelle.veteres.model.Tile;
import nl.bartpelle.veteres.model.entity.PathQueue;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.model.entity.player.Privilege;
import nl.bartpelle.veteres.model.entity.player.Skills;
import nl.bartpelle.veteres.model.map.MapObj;
import nl.bartpelle.veteres.net.message.game.Action;
import nl.bartpelle.veteres.net.message.game.PacketInfo;
import nl.bartpelle.veteres.plugin.impl.LoginPlugin;
import nl.bartpelle.veteres.plugin.impl.ObjectFirstClickPlugin;

/**
 * Created by Bart on 8/23/2015.
 * Modified by Simon on 4/3/2016.
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

            player.walkTo(obj, PathQueue.StepType.REGULAR);
            player.faceObj(obj);


            Tile targetTile = player.pathQueue().getTargetTile();
            player.world().getEventHandler().addEvent(player, new ClickObjectEvent(player, obj, targetTile));



            /*player.world().getEventHandler().addEvent(player, 1, new Event() {
                @Override
                public void execute(EventContainer container) {
                    if (player.tile().equals(lastTile)) {
                        container.stop();
                    }
                }

                @Override
                public void stop() {
                    handleObject(player, obj);
                }
            });*/
        }
    }

    private void handleObject(Player player, MapObj obj) {
        switch (id) {
            case 6817: // Prayer altar
                player.skills().restorePrayer();
                player.animate(645);
                player.message("You have recharged your prayer.");
                break;
            case 6552: // Spellbook altar
                //player.varps().varbit(4070, 0);
                //player.varps().varbit(4070, 1);
                //player.varps().varbit(4070, 2);

                // player.animate(645);

                player.message("TODO");
                //TODO
                //need to sendchatboxinterface and find interface for dialogue
                break;
            case 10084: // Draynor wall climb
                handleDraynorWallClimb(player);
                break;
            case 23271: // Wilderness ditch
                handleWildernessDitch(player);
                break;

        }

    }

    private void handleWildernessDitch(Player player) {
        boolean below = player.tile().z <= 3520;
        int targetY = (below ? 3523 : 3520);
        player.teleport(player.tile().x, targetY);
        /*player.event(new Event() {
            int count = 0;

            @Override
            public void execute(EventContainer container) {
                switch (count) {
                    case 0:
                        player.lock();
                        int x = player.tile().x;
                        player.pathQueue().step(x, targetY);
                        player.faceTile(new Tile(x, targetY));
                        player.animate(2586);
                        player.forceMove(new ForceMovement(0, 1, 25, 30));
                        break;
                    case 1:
                        player.animate(2588);
                        break;
                    case 2:
                        x = player.tile().x;
                        player.pathQueue().step(x, targetY);
                        player.forceMove(new ForceMovement(0, 1, 17, 26));
                        break;
                    default:
                        container.stop();
                        break;
                }
                count++;
            }

            @Override
            public void stop() {
                player.animate(-1);
                player.unlock();
            }
        });*/
    }

    /**
     * TODO: Fix concurrent modification exception.
     * @param player
     */
    private void handleDraynorWallClimb(Player player) {
        // Is the player on the correct side of the wall?
        if (player.tile().z <= 3255)
            return;

        // Start event
        player.event(new Event() {
            int count = 0;

            @Override
            public void execute(EventContainer container) {
                switch (count) {
                    case 0:
                        player.lock();
                        player.pathQueue().step(3088, 3256);
                        player.animate(2583, 20);
                        player.forceMove(new ForceMovement(0, 1, 25, 30));
                        break;
                    case 1:
                        player.animate(2585);
                        break;
                    case 2:
                        player.pathQueue().step(3088, 3255);
                        player.forceMove(new ForceMovement(0, 1, 17, 26));
                        break;
                    case 3:
                        player.skills().addXp(Skills.AGILITY, 10.0);
                        player.unlock();
                        container.stop();
                        break;
                }
                count++;
            }
        });
    }

}
