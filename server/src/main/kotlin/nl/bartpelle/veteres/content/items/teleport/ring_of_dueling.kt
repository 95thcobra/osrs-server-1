/**
 * Created by bart on 9/8/15.
 */
package nl.bartpelle.veteres.content.items.teleport

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by bart on 9/8/15.
 */
@ScriptMain fun ring_of_duelling(r: ScriptRepository) {
    for (i in 2552..2566 step 2) {
        r.onItemOption4(i) @Suspendable {rub_duel_ring(it)}
        r.onEquipmentOption(1, i) @Suspendable {do_duel_ring(Tile(3315, 3235), it, true)}
        r.onEquipmentOption(2, i) @Suspendable {do_duel_ring(Tile(2440, 3090), it, true)}
        r.onEquipmentOption(3, i) @Suspendable {do_duel_ring(Tile(3388, 3160), it, true)}

    }
}

@Suspendable fun rub_duel_ring(script: Script) {
    script.message("You rub the ring...")

    when (script.options("Al Kharid Duel Arena.", "Castle Wars Arena.", "Clan Wars Arena.", "Nowhere.", title = "Where would you like to teleport to?")) {
        1 -> do_duel_ring(Tile(3315, 3235), script)
        2 -> do_duel_ring(Tile(2440, 3090), script)
        3 -> do_duel_ring(Tile(3388, 3160), script)
    }
}

@Suspendable fun do_duel_ring(target: Tile, script: Script, equipment: Boolean = false) {
    val slot: Int = script.player()[AttributeKey.ITEM_SLOT]
    val item = (if (equipment) script.player().equipment() else script.player().inventory()) [slot]
    val charges = (7 - (item.id() - 2552) / 2) + 1

    script.player().lock()
    script.player().animate(714)
    script.player().graphic(111, 92, 0)
    script.delay(3)
    script.player().teleport(script.player().world().randomTileAround(target, 2))
    script.player().animate(-1)

    if (charges == 1)
        script.message("Your ring of dueling crumbles to dust.".col("7F00FF"))
    else if (charges == 2)
        script.message("Your ring of dueling has one charge left.".col("7F00FF"))
    else
        script.message("Your ring of dueling has ${numToStr(charges-1)} charges left.".col("7F00FF"))

    var result: Item? = Item(item.id() + 2)
    if (item.id() == 2566)
        result = null

    if (equipment) {
        script.player().equipment()[slot] = result
    } else {
        script.player().inventory()[slot] = result
    }

    script.player().unlock()
}