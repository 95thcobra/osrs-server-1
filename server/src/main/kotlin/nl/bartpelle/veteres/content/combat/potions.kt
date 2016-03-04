package nl.bartpelle.veteres.content.combat

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.skills.prayer.Bone
import nl.bartpelle.veteres.content.skills.prayer.bury
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.script.TimerKey

/**
 * Created by Carl on 2015-08-14.
 */

enum class Potion(val skill: Int, val base: Int, val percentage: Int, val defaultalgo: Boolean, val message: String, vararg val ids: Int) {
    ATTACK_POTION(Skills.ATTACK, 3, 10, true, "Attack potion", 2428, 121, 123, 125),
    SUPER_ATTACK_POTION(Skills.ATTACK, 5, 15, true, "Super attack potion", 2436, 145, 147, 149),
    STRENGTH_POTION(Skills.STRENGTH, 3, 10, true, "Strength potion", 113, 115, 117, 119),
    SUPER_STRENGTH_POTION(Skills.STRENGTH, 5, 15, true, "Super strength potion", 2440, 157, 159, 161),
    DEFENCE_POTION(Skills.DEFENCE, 3, 10, true, "Defence potion", 2432, 133, 135, 137),
    SUPER_DEFENCE_POTION(Skills.DEFENCE, 5, 15, true, "Super defence potion", 2442, 163, 165, 167),
    MAGIC_POTION(Skills.MAGIC, 3, 10, true, "Magic potion", 3040, 3042, 3044, 3046),
    SUPER_MAGIC_POTION(Skills.MAGIC, 2, 12, true, "Super magic potion", 11726, 11727, 11728, 11729),
    RANGING_POTION(Skills.RANGED, 3, 10, true, "Ranging potion", 2444, 169, 171, 173),
    SUPER_RANGING_POTION(Skills.RANGED, 2, 12, true, "Super ranging potion", 11722, 11723, 11724, 11725),
    SARADOMIN_BREW(-1, -1, -1, false, "Saradomin brew", 6685, 6687, 6689, 6691),
    SUPER_RESTORE(-1, -1, -1, false, "Super restore", 3024, 3026, 3028, 3030),
    RESTORE_POTION(-1, -1, -1, false, "Restore potion", 2430, 127, 129, 131),
    COMBAT_POTION(-1, -1, -1, false, "Combat potion", 9739, 9741, 9743, 9745);

    fun nextDose(id: Int): Int {
        for (i in 0..ids.size) {
            if (ids[i] == id)
                return ids[i + 1]
        }
        return 0
    }

    fun isLastDose(id: Int): Boolean {
        return ids[ids.size - 1] == id
    }

    fun dosesLeft(id: Int): Int {
        for (i in 0..ids.size) {
            if (ids[i] == id)
                return ids.size - i - 1
        }
        return 0
    }

}

@ScriptMain fun potions(repo: ScriptRepository) {
    Potion.values().forEach { pot ->
        pot.ids.forEach { id ->
            repo.onItemOption1(id) @Suspendable {
                consume(it, pot, id)
            }
        }
    }
}

fun consume(script: Script, potion: Potion, id: Int) {
    if (script.player().timers().has(TimerKey.POTION))
        return

    script.player().timers().register(TimerKey.POTION, 3)
    script.player().animate(829)
    script.player().message("You drink some of your " + potion.message + ".")
    deductDose(script, potion, id)
}

fun deductDose(script: Script, potion: Potion, id: Int) {
    if (potion.isLastDose(id)) {
        script.player().inventory()[script.player().attrib(AttributeKey.ITEM_SLOT)] = Item(229)

        script.player().message("You have finished your potion.")
    } else {
        script.player().inventory()[script.player().attrib(AttributeKey.ITEM_SLOT)] = Item(potion.nextDose(id))
        val left = potion.dosesLeft(id)

        var doses = if (left == 1) "dose" else "doses"
        script.player().message("You have $left $doses of your potion left.")
    }

    if (potion.defaultalgo) {
        val change = potion.base.toDouble() + (script.player().skills().xpLevel(potion.skill).toDouble() * potion.percentage / 100.0)
        script.player().skills().alterSkill(potion.skill, change.toInt(), false)
    } else if (potion == Potion.SARADOMIN_BREW) {

        val heal = (script.player().skills().xpLevel(Skills.HITPOINTS) * 0.15).toInt() + 2;
        val def = (script.player().skills().xpLevel(Skills.DEFENCE) * 0.2).toInt() + 2;

        script.player().heal(heal)
        script.player().skills().alterSkill(Skills.DEFENCE, def, false);

        val ids = intArrayOf(Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGED);

        for (i in ids) {
            val lvl = script.player().skills().xpLevel(i);
            script.player().skills().alterSkill(i, -(lvl * 0.1).toInt(), true)
        }

    } else if (potion == Potion.SUPER_RESTORE) {
        for (i in 0..Skills.SKILL_COUNT - 1) {
            if (i != Skills.HITPOINTS) {
                val current_flat = script.player().skills().xpLevel(i)
                val current = script.player().skills().level(i)
                var restorable = 8 + (current_flat * 25) / 100;

                if (current + restorable > current_flat)
                    restorable = current_flat - current;
                else
                    restorable = current + restorable;

                if (current < current_flat)
                    script.player().skills().alterSkill(i, restorable, true)
            }
        }
    } else if (potion == Potion.RESTORE_POTION) {
        for (i in 0..Skills.SKILL_COUNT - 1) {
            if (i != Skills.HITPOINTS && i != Skills.PRAYER) {
                val current_flat = script.player().skills().xpLevel(i)
                val current = script.player().skills().level(i)
                var restorable = 10 + (current_flat * 30) / 100;

                if (current + restorable > current_flat)
                    restorable = current_flat - current;
                else
                    restorable = current + restorable;

                if (current < current_flat)
                    script.player().skills().alterSkill(i, restorable, true)
            }
        }
    } else if (potion == Potion.COMBAT_POTION) {
        val curStr = script.player().skills().xpLevel(Skills.STRENGTH)
        val curAtk = script.player().skills().xpLevel(Skills.ATTACK)

        script.player().skills().alterSkill(Skills.ATTACK, (curAtk * 0.1).toInt() + 3, false)
        script.player().skills().alterSkill(Skills.STRENGTH, (curStr * 0.1).toInt() + 3, false)
    }
}

@ScriptMain fun statreplenish(repo: ScriptRepository) {
    repo.onLogin { it.player().timers()[TimerKey.STAT_REPLENISH] = 100 }
    repo.onTimer(TimerKey.STAT_REPLENISH) {
        it.player().skills().replenishStats()
        it.player().timers()[TimerKey.STAT_REPLENISH] = 100
    }
}