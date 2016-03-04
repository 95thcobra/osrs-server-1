package nl.bartpelle.veteres.content.skills.agility

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.animate
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.waitForTile
import nl.bartpelle.veteres.model.ForceMovement
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.entity.PathQueue
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 9/4/2015.
 */
@ScriptMain fun draynor_rooftop(r: ScriptRepository) {
    // Wall climb
    r.onObject(10073) @Suspendable {
        it.player().lock()
        it.delay(1)
        it.player().animate(828, 15)
        it.delay(2)
        it.player().teleport(3102, 3279, 3)
        it.animate(-1)
        it.player().skills().addXp(Skills.AGILITY, 5.0)
        it.player().unlock()
    }

    // Tightrope
    r.onObject(10074) @Suspendable {
        it.player().lock()
        it.player().pathQueue().clear()
        it.player().pathQueue().interpolate(3090, 3277, PathQueue.StepType.FORCED_WALK)
        it.delay(1)
        it.player().looks().render(763, 762, 762, 762, 762, 762, -1)
        it.waitForTile(Tile(3090, 3277))
        it.player().looks().resetRender()
        it.player().pathQueue().interpolate(3090, 3276)
        it.delay(1)
        it.player().skills().addXp(Skills.AGILITY, 8.0)
        it.player().unlock()
    }

    // Second tightrope
    r.onObject(10075) @Suspendable {
        it.delay(1)
        it.player().lock()
        it.player().pathQueue().clear()
        it.player().pathQueue().interpolate(3092, 3276, PathQueue.StepType.FORCED_WALK)
        it.delay(1)
        it.player().looks().render(763, 762, 762, 762, 762, 762, -1)
        it.player().pathQueue().interpolate(3092, 3266, PathQueue.StepType.FORCED_WALK)
        it.waitForTile(Tile(3092, 3267))
        it.player().looks().resetRender()
        it.delay(1)
        it.player().skills().addXp(Skills.AGILITY, 7.0)
        it.player().unlock()
    }

    // Narrow wall
    r.onObject(10077) @Suspendable {
        it.delay(1)
        it.player().lock()
        it.player().animate(753)
        it.player().looks().render(757, 757, 756, 756, 756, 756, -1)
        it.player().pathQueue().clear()
        it.player().pathQueue().interpolate(3089, 3262, PathQueue.StepType.FORCED_WALK)
        it.player().pathQueue().interpolate(3088, 3261, PathQueue.StepType.FORCED_WALK)
        it.waitForTile(Tile(3088, 3261))
        it.player().looks().resetRender()
        it.player().skills().addXp(Skills.AGILITY, 7.0)
        it.animate(759)
        it.delay(1)
        it.player().unlock()
    }

    // Wall jump
    r.onObject(10084) @Suspendable {
        it.delay(1)
        it.player().lock()
        it.player().pathQueue().step(3088, 3256)
        it.player().animate(2583, 20)
        it.player().forceMove(ForceMovement(0, 1, 25, 30))
        it.delay(1)
        it.player().animate(2585)
        it.delay(1)
        it.player().pathQueue().step(3088, 3255)
        it.player().forceMove(ForceMovement(0, 1, 17, 26))
        it.delay(1)
        it.player().skills().addXp(Skills.AGILITY, 10.0)
        it.player().unlock()
    }

    // Gap jump
    r.onObject(10085) @Suspendable {
        it.delay(1)
        it.player().lock()
        it.player().faceTile(Tile(3096, 3256))
        it.delay(1)
        it.player().animate(2586, 15)
        it.delay(1)
        it.player().teleport(Tile(3096, 3256, 3))
        it.player().animate(2588)
        it.delay(1)
        it.player().animate(-1)
        it.player().skills().addXp(Skills.AGILITY, 4.0)
        it.player().unlock()
    }

    // Crate jump
    r.onObject(10086) @Suspendable {
        it.delay(1)
        it.player().lock()
        it.player().animate(2586, 15)
        it.delay(1)
        it.player().teleport(Tile(3102, 3261, 1))
        it.player().animate(2588)
        it.delay(1)
        it.player().animate(-1)
        it.delay(1)
        it.player().animate(2586, 15)
        it.delay(1)
        it.player().teleport(Tile(3103, 3261, 0))
        it.player().animate(2588)
        it.player().skills().addXp(Skills.AGILITY, 79.0)
        it.delay(1)
        it.player().animate(-1)
        it.player().unlock()
    }
}