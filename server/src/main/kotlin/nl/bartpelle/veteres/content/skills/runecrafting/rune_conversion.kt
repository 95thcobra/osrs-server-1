package nl.bartpelle.veteres.content.skills.runecrafting

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.animate
import nl.bartpelle.veteres.content.contains
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Carl on 2015-08-26.
 */

enum class Altar(val levelReq: Int, val xp: Double, val talisman: Int, val rune: Int, val altarObj: Int, val entranceObj: Int, val entranceTile: Tile, val pure: Boolean, val multiplier: Int) {

    AIR(1, 5.0, 1438, 556, 14897, 2478, Tile(2841, 4830), false, 11),
    MIND(2, 5.5, 1448, 558, 14898, 2453, Tile(2792, 4827), false, 14),
    WATER(5, 6.0, 1444, 555, 14899, 2454, Tile(2712, 4836), false, 19),
    EARTH(9, 6.5, 1440, 557, 14900, 2455, Tile(2655, 4830), false, 29),
    FIRE(14, 7.0, 1442, 554, 14901, 2456, Tile(2577, 4845), false, 35),
    BODY(20, 7.5, 1446, 559, 14902, 2457, Tile(2521, 4834), false, 46),
    COSMIC(27, 8.0, 1454, 564, 14903, 2458, Tile(2162, 4833), true, 59),
    LAW(54, 9.5, 1458, 563, 14904, 2459, Tile(2464, 4818), true, 200),
    NATURE(44, 9.0, 1462, 561, 14905, 2460, Tile(2398, 4841), true, 91),
    CHAOS(35, 8.5, 1452, 562, 14906, 2461, Tile(2281, 4837), true, 74),
    DEATH(65, 10.0, 1456, 560, 14907, 2462, Tile(2208, 4830), true, 200),
    ASTRAL(40, 8.7, -1, 9075, 14911, -1, Tile(2156, 3863), true, 42);
}

@ScriptMain fun rune_conversion(repo: ScriptRepository) {
    Altar.values().forEach { altar ->
        repo.onObject(altar.altarObj) @Suspendable {
            convert(it, altar)
        }
    }

    Altar.values().forEach { altar ->
        repo.onObject(altar.entranceObj) @Suspendable {
            teleport(it, altar)
        }
    }
}

fun teleport(script: Script, altar: Altar) {
    if (!script.player().inventory().contains(altar.talisman)) {
        script.message("You need a " + altar.name().toLowerCase() + " talisman to access the " + altar.name().toLowerCase() + " altar.")
        return
    }

    script.message("You feel a powerful force take hold of you.")
    script.player().lock()
    script.delay(1)
    script.player().teleport(altar.entranceTile)
    script.player().unlock()
}

fun convert(script: Script, altar: Altar) {
    if (script.player().skills().xpLevel(Skills.RUNECRAFTING) < altar.levelReq) {
        script.message("You need a Runecrafting level of " + altar.levelReq + " to infuse these runes.")
        return
    }

    var amount = script.player().inventory().count(7936)
    if (!altar.pure)
        amount += script.player().inventory().count(1436)

    var msg = "pure"
    if (!altar.pure)
        msg = "rune"

    if (amount < 1) {
        script.message("You do not have any $msg essence to bind.")
        return
    } else {
        if (altar.pure)
            script.player().inventory().remove(Item(7936, amount), true)
        else {
            script.player().inventory().remove(Item(7936, script.player().inventory().count(7936)), true)
            script.player().inventory().remove(Item(1436, script.player().inventory().count(1436)), true)
        }
    }

    var multi = 1
    for (i in altar.multiplier..altar.multiplier * 10 step altar.multiplier) {
        if (script.player().skills().xpLevel(Skills.RUNECRAFTING) >= i)
        multi++;
    }

    script.player().skills().addXp(Skills.RUNECRAFTING, altar.xp * amount)
    script.player().inventory().add(Item(altar.rune, amount * multi), false)

    script.animate(791)
    script.player().graphic(186)
}


