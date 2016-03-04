package nl.bartpelle.veteres.content.areas.edgeville

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.ForceMovement
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 8/23/2015.
 * Modified by Sky on 25/2/2016.
 */
@ScriptMain fun wildy_ditch(repo: ScriptRepository) {
    repo.onObject(23271) @Suspendable {
        /*val below = it.player().tile().z <= 3520
        it.player().lock()
        it.delay(1)
        val target = Tile(it.player().tile().x, if (below) 3523 else 3520)
        it.player().teleport(target)
        it.player().unlock()
*/
        //TODO: use forcemovement

        val below = it.player().tile().z <= 3520
        val targetY = if (below) 3523 else 3520

       // it.delay(1)
        it.player().lock()
        it.player().faceTile(Tile(it.player().tile().x, targetY))
        it.delay(1)
        it.player().animate(2586, 15)
        it.delay(1)
        it.player().teleport(Tile(it.player().tile().x, targetY))
        it.player().animate(2588)
        it.delay(1)
        it.player().animate(-1)
        it.player().unlock()
    }
}