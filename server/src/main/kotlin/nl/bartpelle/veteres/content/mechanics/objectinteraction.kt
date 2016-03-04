package nl.bartpelle.veteres.content.mechanics

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.waitForTile
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.GroundItem
import nl.bartpelle.veteres.model.entity.PathQueue
import nl.bartpelle.veteres.model.map.MapObj
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 8/23/2015.
 */
@Suspendable class ObjectInteraction {
    @Suspendable companion object {
        public val script: Function1<Script, Unit> = @Suspendable {
            it: Script ->
            val obj: MapObj? = it.player().attrib(AttributeKey.INTERACTION_OBJECT)
            val option: Int = it.player().attrib(AttributeKey.INTERACTION_OPTION)

            if (obj != null) {
                val reachable = it.player().walkTo(obj, PathQueue.StepType.REGULAR)

                val lastTile = it.player().pathQueue().peekLast()?.toTile() ?: it.player().tile()
                it.waitForTile(lastTile)

                // Did we arrive there?
                it.player().faceObj(obj)
                if (reachable) {
                    if (it.player().attrib(AttributeKey.DEBUG))
                        it.message("Obj %d rot %d type %d", obj.id(), obj.rot(), obj.type())
                    if (!it.player().world().server().scriptRepository().triggerObject(it.player(), obj, option))
                        it.message("Nothing interesting happens.")
                        it.message("Obj %d rot %d type %d", obj.id(), obj.rot(), obj.type())
                } else {
                    it.message("You can't reach that!")
                }
            }
        }
    }
}