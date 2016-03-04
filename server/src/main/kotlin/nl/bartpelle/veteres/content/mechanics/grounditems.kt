package nl.bartpelle.veteres.content.mechanics

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.veteres.content.animate
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.waitForTile
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.GroundItem
import nl.bartpelle.veteres.model.entity.PathQueue
import nl.bartpelle.veteres.script.TimerKey
import nl.bartpelle.veteres.util.Varp

/**
 * Created by Bart on 8/23/2015.
 */
@Suspendable class GroundItemTaking {
    @Suspendable companion object {
        public val script: Function1<Script, Unit> = @Suspendable {
            val item: GroundItem? = it.player().attrib(AttributeKey.GROUNDITEM_TARGET)

            if (item != null) {
                it.player().walkTo(item.tile().x, item.tile().z, PathQueue.StepType.REGULAR)
                it.waitForTile(item.tile())

                // Did we arrive there?
                if (item.tile().equals(it.player().tile())) {
                    // Is the item still valid?
                    if (it.player().world().groundItemValid(item) && it.player().inventory().add(item.item(), false).success()) {
                        it.player().world().removeGroundItem(item)
                        it.player().sound(2582, 0)
                    }
                }
            }
        }
    }
}