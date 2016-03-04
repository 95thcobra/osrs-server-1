package nl.bartpelle.veteres.content.mechanics

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.PathQueue
import nl.bartpelle.veteres.model.entity.Player
import kotlin.platform.platformStatic

/**
 * Created by Bart on 8/12/2015.
 */
@Suspendable class PlayerFollowing {
    @Suspendable companion object {
        public val script: Function1<Script, Unit> = @Suspendable {
            val index: Int = it.player().attrib(AttributeKey.TARGET, 0)
            var player: Player? = it.player().world().players().get(index)

            if (player != null)
                it.player().face(player)

            while (player != null) {
                val last = player.pathQueue().lastStep() ?: break;

                if (!it.player().frozen() && !player.stunned()) // Frozen check (method does this too, but to avoid the endless msgs)
                    it.player().walkTo(last.x, last.z, PathQueue.StepType.REGULAR)
                it.delay(1)

                player = it.player().world().players().get(index)
            }
        }
    }
}