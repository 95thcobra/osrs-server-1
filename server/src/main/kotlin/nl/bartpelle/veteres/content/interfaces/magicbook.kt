package nl.bartpelle.veteres.content.interfaces

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.animate
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.script.TimerKey

/**
 * Created by Carl on 2015-08-12.
 */

@ScriptMain fun magicbook(repo: ScriptRepository) {
    repo.onButton(218, 1, @Suspendable {
        it.animate(4847)
        it.player().graphic(800)
        it.delay(7)
        it.animate(4850)
        it.delay(5)
        it.animate(4853)
        it.player().graphic(802)
        it.delay(3)
        it.animate(4855)
        it.player().graphic(803, -1, 0)
        it.delay(3)
        it.player().graphic(804)
        it.delay(1)
        it.animate(4857)
        it.delay(3)
        it.player().teleport(3088, 3505, 0)
    })

    repo.onButton(218, 16) @Suspendable {castSpell(it, Tile(3212, 3424, 0), Item(556, 3), Item(554, 1), Item(563, 1))} //Varrock teleport
    repo.onButton(218, 19) @Suspendable {castSpell(it, Tile(3222, 3219, 0), Item(556, 3), Item(557, 1), Item(563, 1))} //Lumbridge teleport
    repo.onButton(218, 22) @Suspendable {castSpell(it, Tile(2965, 3383, 0), Item(556, 3), Item(555, 1), Item(563, 1))} //Falador teleport
    repo.onButton(218, 27) @Suspendable {castSpell(it, Tile(2757, 3478, 0), Item(556, 5), Item(563, 1))} //Camelot teleport
    repo.onButton(218, 33) @Suspendable {castSpell(it, Tile(2662, 3306, 0), Item(555, 2), Item(563, 2))} //Ardougne teleport

    repo.onButton(218, 132) @Suspendable {

        if (!it.player().locked()) {

            if (!it.player().timers().has(TimerKey.VENGEANCE_COOLDOWN)) {
                it.player().timers().register(TimerKey.VENGEANCE_COOLDOWN, 50)
                it.player().putattrib(AttributeKey.VENGEANCE_ACTIVE, true)
                it.animate(4410)
                it.player().graphic(726, 100, 0)
            } else {
                it.message("You can only cast vengeance spells every 30 seconds.")
            }
        }
    }
}

@Suspendable fun castSpell(script: Script, tile: Tile, vararg runes: Item) {
    for (rune in runes) { // Check if we have all the runes
        if (!hasRunes(script.player(), rune.id(), rune.amount()))
            return
    }

    for (rune in runes) // Remove all the runes :-)
        script.player().inventory().remove(rune, true)

    script.player().lock()
    script.player().animate(714) // Swooosh
    script.player().graphic(111, 92, 0)
    script.delay(4)
    script.player().teleport(findTile(tile.x, tile.z, tile.level))
    script.animate(-1)
    script.player().graphic(-1)
    script.player().unlock()
}

fun findTile(x: Int, z: Int, level: Int): Tile {
    val original = Tile(x, z, level);
    return original
}

fun hasRunes(player: Player, rune: Int, req: Int): Boolean {
    val has = player.inventory().count(rune)
    if (has < req) {
        val name = Item(rune).definition(player.world()).name.replace("rune", "Runes")
        player.message("You do not have enough $name to cast this spell.")
        return false
    }
    return true
}

fun hasLevel(player: Player, level: Int): Boolean {
    if (player.skills().level(Skills.MAGIC) < level) {
        player.message("Your Magic level is not high enough for this spell.")
        return false
    }
    return true
}