package nl.bartpelle.veteres.content.skills.herblore

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.animate
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.skills.prayer.Bone
import nl.bartpelle.veteres.content.skills.prayer.bury
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.script.TimerKey

/**
 * @author William Talleur <talleurw@gmail.com>
 * @date October 31, 2015
 */
enum class Herb(val itemId : Int, val xp : Double, val level : Int, val cleanId : Int) {
    GUAM(199, 2.5, 3, 249),
    MARRENTILL(201, 3.8, 5, 251),
    TARROMIN(203, 5.0, 11, 253),
    HARRALANDER(205, 6.3, 20, 255),
    RANARR(207, 7.5, 25, 257),
    TOADFLAX(3049, 8.0, 30, 2998),
    SPIRIT_WEED(12174, 7.8, 35, 12172),
    IRIT(209, 8.8, 40, 259),
    WERGALI(14836, 9.5, 41, 14854),
    AVANTOE(211, 10.0, 48, 261),
    KWUARM(213, 11.3, 54, 263),
    SNAPDRAGON(3051, 11.8, 59, 3000),
    CADANTINE(215, 12.5, 65, 265),
    LANTADYME(2485, 13.1, 67, 2481),
    DWARF_WEED(217, 13.8, 70, 267),
    TORSTOL(219, 15.0, 75, 269);
}

@Suspendable fun clean(script : Script, herb : Herb) {
    if (script.player().timers().has(TimerKey.HERB_CLEANING))
        return

    if (script.player().skills().level(Skills.HERBLORE) < herb.level) {
        script.player().message("You need level " + herb.level + " to clean a " + Item(herb.cleanId).definition(script.player().world()).name + ".")
        return
    }

    script.player().inventory().remove(Item(herb.itemId), true, script.player().attrib(AttributeKey.ITEM_SLOT, 0))
    script.player().inventory().add(Item(herb.cleanId), true)
    script.player().timers().register(TimerKey.HERB_CLEANING, 1)
    script.delay(1)
    script.message("You clean the " + Item(herb.cleanId).definition(script.player().world()).name.toLowerCase() + ".")
    script.player().skills().addXp(Skills.HERBLORE, herb.xp)
}

@ScriptMain fun cleaning(repo: ScriptRepository) {
    Herb.values.forEach { herb -> repo.onItemOption1(herb.itemId) @Suspendable { clean(it, herb) } }
}