package nl.bartpelle.veteres.content.combat

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.script.TimerKey
import nl.bartpelle.veteres.util.Varp
import nl.bartpelle.veteres.content.*

/**
 * Created by Bart on 8/16/2015.
 */
@ScriptMain fun specialenergy(repo: ScriptRepository) {
    repo.onLogin @Suspendable { it.player().timers()[TimerKey.SPECIAL_ENERGY_RECHARGE] = 50 }

    repo.onTimer(TimerKey.SPECIAL_ENERGY_RECHARGE) @Suspendable {
        if (it.player().varps()[Varp.SPECIAL_ENERGY] < 1000) {
            val set = it.player().varps()[Varp.SPECIAL_ENERGY] + 100
            it.player().varps()[Varp.SPECIAL_ENERGY] = Math.min(1000, set)
        }

        it.player().timers()[TimerKey.SPECIAL_ENERGY_RECHARGE] = 50
    }
}