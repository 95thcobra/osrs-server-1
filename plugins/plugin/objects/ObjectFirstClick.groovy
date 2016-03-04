package plugin.objects

import nl.bartpelle.veteres.event.Event
import nl.bartpelle.veteres.event.EventContainer
import nl.bartpelle.veteres.fs.ItemDefinition
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.entity.PathQueue
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.map.MapObj
import nl.bartpelle.veteres.plugin.PluginListener
import nl.bartpelle.veteres.plugin.PluginSignature
import nl.bartpelle.veteres.plugin.impl.ObjectFirstClickPlugin

@PluginSignature(ObjectFirstClickPlugin.class)
final class ObjectFirstClick implements PluginListener<ObjectFirstClickPlugin> {

    @Override
    void execute(Player player, ObjectFirstClickPlugin context) {
        MapObj obj = player.attrib(AttributeKey.INTERACTION_OBJECT)

        switch (context.id) {

            case 6817: // Prayer altar
                player.faceObj(obj)
                player.walkTo(obj, PathQueue.StepType.REGULAR)

               // player.delay(3)

                //TODO: delay(1)
               /* Tile lastTile = player.pathQueue().peekLast()?.toTile() ?: player.tile()

                player.skills().restorePrayer()
                player.animate(645)
*/
                Tile lastTile = player.pathQueue().peekLast()?.toTile() ?: player.tile()
                player.world().getEventHandler().addEvent(player, 3, new Event() {
                    @Override
                    public void execute(EventContainer container) {
                        if (player.tile() == lastTile) {
                            container.stop();
                        }
                    }

                    @Override
                    public void stop() {
                        player.skills().restorePrayer()
                        player.animate(645)
                    }
                });
        }
    }
}