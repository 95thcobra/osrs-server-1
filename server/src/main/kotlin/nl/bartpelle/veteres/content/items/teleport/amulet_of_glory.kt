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
 * Created by bart on 9/6/15.
 */
enum class GloryAmulet(val id: Int, val charges: Int, val result: Int) {
    SIX(11978, 6, 11976),
    FIVE(11976, 5, 1712),
    FOUR(1712, 4, 1710),
    THREE(1710, 3, 1708),
    TWO(1708, 2, 1706),
    ONE(1706, 1, 1704)
}

@ScriptMain fun amulet_of_glory(r: ScriptRepository) {
    GloryAmulet.values().forEach { g ->
        r.onItemOption4(g.id) @Suspendable {rub(it, g.result, g.charges)}

        r.onEquipmentOption(1, g.id) @Suspendable {doMagic(Tile(3087, 3496), it, g.result, g.charges, true)}
        r.onEquipmentOption(2, g.id) @Suspendable {doMagic(Tile(2918, 3176), it, g.result, g.charges, true)}
        r.onEquipmentOption(3, g.id) @Suspendable {doMagic(Tile(3105, 3251), it, g.result, g.charges, true)}
        r.onEquipmentOption(4, g.id) @Suspendable {doMagic(Tile(3293, 3163), it, g.result, g.charges, true)}
    }

    r.onEquipmentOption(1, 1704) {
        it.player().message("The amulet has lost its charge.")
        it.player().message("It will need to be recharged before you can use it again.")
    }

    r.onItemOption4(1704) {
        it.player().message("The amulet has lost its charge.")
        it.player().message("It will need to be recharged before you can use it again.")
    }
}

@Suspendable fun rub(script: Script, result: Int, currentCharges: Int) {
    script.message("You rub the amulet...")

    when (script.options("Edgeville", "Karamja", "Draynor Village", "Al Kharid", "Nowhere", title = "Where would you like to teleport to?")) {
        1 -> doMagic(Tile(3087, 3496), script, result, currentCharges)
        2 -> doMagic(Tile(2918, 3176), script, result, currentCharges)
        3 -> doMagic(Tile(3105, 3251), script, result, currentCharges)
        4 -> doMagic(Tile(3293, 3163), script, result, currentCharges)
    }
}

@Suspendable fun doMagic(target: Tile, script: Script, result: Int, currentCharges: Int, equipment: Boolean = false) {
    val slot: Int = script.player()[AttributeKey.ITEM_SLOT]
    script.player().lock()
    script.player().animate(714);
    script.player().graphic(111, 92, 0)
    script.delay(3)
    script.player().teleport(target)
    script.player().animate(-1)

    if (currentCharges == 1)
        script.message("You use your amulet's last charge.".col("7F00FF"))
    else if (currentCharges == 2)
        script.message("Your amulet has one charge left.".col("7F00FF"))
    else
        script.message("Your amulet has ${numToStr(currentCharges-1)} charges left.".col("7F00FF"))

    if (equipment) {
        script.player().equipment()[slot] = Item(result)
    } else {
        script.player().inventory()[slot] = Item(result)
    }

    script.player().unlock()
}

fun numToStr(num: Int): String = when(num) {
    2 -> "two"
    3 -> "three"
    4 -> "four"
    5 -> "five"
    6 -> "six"
    7 -> "seven"
    else -> "?"
}