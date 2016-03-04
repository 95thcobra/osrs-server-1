package nl.bartpelle.veteres.content.npcs.bosses

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.npc
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 8/28/2015.
 */
@ScriptMain fun callisto(r: ScriptRepository) {
    r.onNpcSpawn(6609) @Suspendable {
        val n = it.npc()
        while (true) {
            // Don't process if dead
            if (n.dead()) {
                it.delay(2)
                continue
            }

            it.npc().animate(4925)
            it.delay(10)
        }
    }
}