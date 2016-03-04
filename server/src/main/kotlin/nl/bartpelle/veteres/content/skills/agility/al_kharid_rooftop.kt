package nl.bartpelle.veteres.content.skills.agility

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.animate
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.waitForTile
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.entity.PathQueue
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 9/5/2015.
 */
@ScriptMain fun al_kharid_rooftop(r: ScriptRepository) {
    // Wall climb
    r.onObject(10093) @Suspendable {
        it.player().lock()
        it.delay(1)
        it.player().animate(828, 15)
        it.delay(2)
        it.player().teleport(3273, 3192, 3)
        it.animate(-1)
        it.player().skills().addXp(Skills.AGILITY, 10.0)
        it.player().unlock()
    }

    // Tightrope
    r.onObject(10284) @Suspendable {
        it.delay(1)
        it.player().lock()
        it.player().pathQueue().clear()
        it.player().pathQueue().interpolate(3272, 3172, PathQueue.StepType.FORCED_WALK)
        it.delay(1)
        it.player().looks().render(763, 762, 762, 762, 762, 762, -1)
        it.waitForTile(Tile(3272, 3173))
        it.player().looks().resetRender()
        it.delay(1)
        it.player().skills().addXp(Skills.AGILITY, 30.0)
        it.player().unlock()
    }

    // Cable swing
    r.onObject(10355) @Suspendable {
        it.delay(1)
        it.player().lock()

    }
}