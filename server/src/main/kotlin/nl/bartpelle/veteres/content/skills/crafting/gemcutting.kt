package nl.bartpelle.veteres.content.skills.crafting

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.handlers.SkillDialogueHandler
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.script.TimerKey

/**
 * @author William Talleur <talleurw@gmail.com>
 * @date November 01, 2015
 */
enum class Gem(val uncutId : Int, val cutId : Int, val xp : Double, val level : Int, val emoteId : Int) {
    OPAL(1625, 1609, 15.0, 1, 886),
    JADE(1627, 1611, 20.0, 13, 886),
    RED_TOPAZ(1629, 1613, 25.0, 16, 887),
    SAPPHIRE(1623, 1607, 50.0, 20, 888),
    EMERALD(1621, 1605, 67.0, 27, 889),
    RUBY(1619, 1603, 72.0, 34, 887),
    DIAMOND(1617, 1601, 85.5, 43, 890),
    DRAGONSTONE(1631, 1615, 105.5, 55, 885),
    ONYX(6571, 6573, 125.5, 91, 2717);
}

// so this is just a function
@Suspendable fun cutgem(script : Script, gem : Gem) {

    if (script.player().timers().has(TimerKey.GEM_CUTTING))
        return

    if (script.player().skills().level(Skills.CRAFTING) < gem.level) {
        script.player().message("You need level " + gem.level + " to cut a " + Item(gem.uncutId).definition(script.player().world()).name + ".")
        return
    }

    script.player().inventory().remove(Item(gem.uncutId), true, script.player().attrib(AttributeKey.ITEM_SLOT, 0))
    script.player().inventory().add(Item(gem.cutId), true)
    script.player().skills().addXp(Skills.CRAFTING, gem.xp)
    script.player().timers().register(TimerKey.GEM_CUTTING, 2)
    script.delay(2)
    script.message("You cut the " + Item(gem.cutId).definition(script.player().world()).name.toLowerCase() + ".")
}

// this is your main which will be imported into the script executor
@ScriptMain fun gemcutting(repo: ScriptRepository) {
    Gem.values.forEach { gem ->
        repo.onItemOnItem(1755, gem.uncutId) @Suspendable {
            var handler: SkillDialogueHandler = object : SkillDialogueHandler(it.player(), SkillDialogueHandler.SkillDialogue.ONE_OPTION, Item(gem.uncutId)) {
                override fun create(amount: Int, index: Int) {
                    cutgem(it, gem)
                }

                override fun getAll(index: Int): Int {
                    return player.inventory().count(gem.uncutId)
                }
            }
            if (it.player().inventory().count(gem.uncutId) == 1) {
                handler.create(0, 1);
            } else {
                handler.open(it.player(), SkillDialogueHandler.SkillDialogue.ONE_OPTION);
            }
        }
    }
}