package nl.bartpelle.veteres.content.skills.agility

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.entity.PathQueue
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 8/29/2015.
 */
@ScriptMain fun agility_shortcuts(r: ScriptRepository) {
    // Al Kharid mine shortcut exit
    r.onObject(16550) @Suspendable {
        if (it.player().skills().level(Skills.AGILITY) < 38) {
            it.message("You need an agility level of 38 to negotiate these rocks.")
        } else {
            it.player().lock()
            it.delay(1)
            it.player().looks().render(738, 737, 737, 737, 737, 737, -1)
            it.player().sound(2244, 10, 4)
            it.player().pathQueue().clear()
            it.player().pathQueue().interpolate(3305, 3315, PathQueue.StepType.FORCED_WALK)
            it.delay(1)
            it.player().sound(2244, 10, 4)
            it.delay(1)
            it.player().sound(2244, 10, 4)
            it.delay(2)
            it.player().looks().resetRender()
            it.player().pathQueue().interpolate(3306, 3315, PathQueue.StepType.FORCED_WALK)
            it.delay(1)
            it.player().unlock()
        }
    }
}