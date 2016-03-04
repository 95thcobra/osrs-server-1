package nl.bartpelle.veteres.content.skills.prayer

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.animate
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.waitForTimer
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.script.TimerKey

/**
 * Created by Carl on 2015-08-12.
 */

enum class Bone(val itemId : Int, val xp : Double) {
    REGULAR_BONES(526, 4.5),
    BURNT_BONES(528, 4.5),
    BAT_BONES(530, 4.5),
    WOLF_BONES(2859, 4.5),
    BIG_BONES(532, 15.0),
    BABYDRAGON_BONES(534, 30.0),
    DRAGON_BONES(536, 72.0),
    ZOGRE_BONES(4812, 22.5),
    OURG_BONES(4834, 140.0),
    WYVERN_BONES(6812, 72.0),
    DAGANNOTH_BONES(6729, 125.0),
    LAVA_DRAGON_BONES(11943, 340.0),
}

@ScriptMain fun bone_burying(repo: ScriptRepository) {
    Bone.values.forEach { bone -> repo.onItemOption1(bone.itemId) @Suspendable {bury(it, bone)} }
}

@Suspendable fun bury(script : Script, bone : Bone) {
    if (script.player().timers().has(TimerKey.BONE_BURYING))
        return

    script.player().inventory().remove(Item(bone.itemId), true, script.player().attrib(AttributeKey.ITEM_SLOT, 0))
    script.player().timers().register(TimerKey.BONE_BURYING, 2)
    script.animate(827)
    script.message("You dig a hole in the ground...")
    script.delay(2)
    script.player().skills().addXp(Skills.PRAYER, bone.xp)
    script.message("You bury the bones.")
}
