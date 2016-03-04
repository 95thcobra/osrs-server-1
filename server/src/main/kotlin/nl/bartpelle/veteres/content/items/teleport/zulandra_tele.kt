package nl.bartpelle.veteres.content.items.teleport

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.util.Varp

/**
 * Created by Bart on 8/25/2015.
 */
@ScriptMain fun zulandra_tele(r: ScriptRepository) {
    r.onItemOption1(12938) @Suspendable {script ->
        val player = script.player()
        if (player.world().emulation() && player.varps()[Varp.REGICIDE_STATE] < 15) {
            player.message("You need to complete the Regicide quest before you can teleport to Zul-Andra.")
        } else {
            player.lock()
            player.inventory() -= 12938
            player.animate(3864)
            player.world().tileGraphic(1039, player.tile(), 0, 0)
            player.sound(200, 10)
            script.delay(3)
            player.teleport(2196, 3056)
            player.animate(-1)
            player.unlock()
        }
    }
}