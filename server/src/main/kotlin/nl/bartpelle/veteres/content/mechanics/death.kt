package nl.bartpelle.veteres.content.mechanics

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.veteres.content.animate
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.script.TimerKey
import nl.bartpelle.veteres.util.ItemsOnDeath
import nl.bartpelle.veteres.util.PkpSystem
import nl.bartpelle.veteres.util.Varp

/**
 * Created by Bart on 8/15/2015.
 */
@Suspendable class Death {
    @Suspendable companion object {
        public val script: Function1<Script, Unit> = @Suspendable {
            it.player().lock()

            val killer = it.player().killer() ?: null

            if (killer != null)
                PkpSystem.handleDeath(killer as Player, it.player())

            it.delay(2)
            it.message("Oh dear, you are dead!")
            it.animate(2304)
            it.delay(4)
            if (killer != null)
                ItemsOnDeath.dropItems(killer as Player, it.player())
            it.player().teleport(3087, 3500)

            // Reset some values
            it.player().skills().resetStats()
            it.player().timers().cancel(TimerKey.FROZEN)
            it.player().timers().cancel(TimerKey.STUNNED)
            it.player().varps().varp(Varp.SPECIAL_ENERGY, 1000)
            it.player().varps().varp(Varp.SPECIAL_ENABLED, 0)
            it.player().damagers().clear() //Clear damagers
            it.player().face(null) // Reset entity facing
            Prayers.disableAllPrayers(it.player())

            it.player().graphic(-1)
            it.player().hp(100, 0)
            it.animate(-1)
            it.delay(1)

            it.player().unlock()
        }
    }
}